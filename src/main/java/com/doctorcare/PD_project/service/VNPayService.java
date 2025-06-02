package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Transaction;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.respository.AppointmentRepository;
import com.doctorcare.PD_project.respository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class VNPayService {

    AppointmentRepository appointmentRepository;
    TransactionRepository transactionRepository;

    String vnp_TmnCode = "HUHESPZP";
    String vnp_HashSecret = "172DW50ZUGR4U19D1WT5QPYIG4LTSMNW";
    String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    String vnp_ReturnUrl = "https://hd-care-front-end.vercel.app/home";

    public String createPaymentUrl(String appointmentId, String ipAddr) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((int)(appointment.getAmount() * 100)));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", appointmentId);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán lịch hẹn " + appointmentId);
        vnp_Params.put("vnp_OrderType", "billpayment");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", ipAddr);
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        String queryUrl = createQueryString(vnp_Params);
        String secureHash = hmacSHA512(vnp_HashSecret, queryUrl);
        queryUrl += "&vnp_SecureHash=" + secureHash;

        Transaction transaction = new Transaction();
        transaction.setAppointment(appointment);
        transaction.setTransactionId(appointmentId);
        transaction.setAmount(appointment.getAmount());
        transaction.setStatus("PENDING");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setPaymentUrl(vnp_Url + "?" + queryUrl);
        System.out.println(vnp_Url + "?" + queryUrl);
        transactionRepository.save(transaction);

        return transaction.getPaymentUrl();
    }

    public boolean refundPayment(String transactionId, String ipAddr) {
        Transaction txn = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "refund");
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_TransactionType", "02");
        params.put("vnp_TransactionId", txn.getTransactionId());
        params.put("vnp_Amount", String.valueOf((int)(txn.getAmount() * 100)));
        params.put("vnp_OrderInfo", "Refund for appointment " + txn.getAppointment().getId());
        params.put("vnp_IpAddr", ipAddr);
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        String query = createQueryString(params);
        String secureHash = hmacSHA512(vnp_HashSecret, query);
        String url = vnp_Url + "?" + query + "&vnp_SecureHash=" + secureHash;

        boolean success = this.callHttpRefundUrl(url);

        txn.setRefundTransactionId(txn.getTransactionId());
        txn.setRefundAmount(txn.getAmount());
        txn.setRefundStatus(success ? "SUCCESS" : "FAILED");
        txn.setRefundedAt(LocalDateTime.now());
        transactionRepository.save(txn);

        if (success) {
            Appointment appt = txn.getAppointment();
            appt.setStatus(AppointmentStatus.REFUNDED.toString());
            appointmentRepository.save(appt);
        }
        return success;
    }

    private boolean callHttpRefundUrl(String url) {
        // TODO: cài HTTP Client hoặc RestTemplate call
        return true;
    }

    private String createQueryString(Map<String, String> params) {
        List<String> fields = new ArrayList<>(params.keySet());
        Collections.sort(fields);
        StringBuilder query = new StringBuilder();
        for (String field : fields) {
            String value = params.get(field);
            if (value != null && !value.isEmpty()) {
                query.append(URLEncoder.encode(field, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                        .append('&');
            }
        }
        return query.substring(0, query.length() - 1);
    }

    private String hmacSHA512(String key, String data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            mac.init(new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA512"));
            byte[] hmac = mac.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hmac) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmacSHA512", e);
        }
    }
}