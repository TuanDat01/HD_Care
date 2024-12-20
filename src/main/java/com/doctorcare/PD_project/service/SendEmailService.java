package com.doctorcare.PD_project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.event.create.OnRegisterEvent;
import com.doctorcare.PD_project.mail.EmailServiceImpl;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SendEmailService {
    private final EmailServiceImpl emailServiceImpl;

    public SendEmailService(EmailServiceImpl emailServiceImpl) {
        this.emailServiceImpl = emailServiceImpl;
    }

    @Async
    public void sendAppointmentConfirmation(Appointment savedAppointment, boolean isCancel, String note) throws MessagingException {
        String title = isCancel ? "Thông báo hủy lịch hẹn" : "Thông báo xác nhận lịch hẹn thành công";
        String reason = (note != null && !note.isEmpty()) ? "<p style=\"margin-top: 10px; color: #d9534f; font-weight: bold;\">Lý do thay đổi lịch hẹn: " + note + "</p>" : "";

        String subjectTime = savedAppointment.getSchedule().getStart().format(DateTimeFormatter.ofPattern("HH:mm"))
                + " - " + savedAppointment.getSchedule().getEnd().format(DateTimeFormatter.ofPattern("HH:mm"));

        String appointmentDate = savedAppointment.getSchedule().getStart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        String htmlContent = String.format(
                "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\">" +
                        "    <div style=\"background-color: #007bff; color: #fff; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;\">" +
                        "        <img src=\"https://res.cloudinary.com/dlokeyspj/image/upload/v1734713330/2024-12-20T23:48:46.7449537000.png\" alt=\"HD_CARE Logo\" style=\"width: 100px; margin-bottom: 10px;\">" +
                        "        <h1 style=\"margin: 0; font-size: 24px;\">%s</h1>" +
                        "    </div>" +
                        "    <div style=\"padding: 20px;\">" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Xin chào <strong>%s</strong>,</p>" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Chúng tôi xin gửi thông báo đến bạn về lịch hẹn tại HD-Care. %s.</p>" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Thông tin chi tiết về lịch hẹn của bạn như sau:</p>" +
                        "        <div style=\"background-color: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0;\">" +
                        "            <p style=\"margin: 5px 0;\"><strong>Thời gian:</strong> %s</p>" +
                        "            <p style=\"margin: 5px 0;\"><strong>Ngày:</strong> %s</p>" +
                        "        </div>" +
                        "        %s" +
                        "        <p style=\"margin: 20px 0; font-size: 16px;\">Chúng tôi luôn mong muốn mang lại trải nghiệm tốt nhất cho bạn. Nếu bạn có bất kỳ câu hỏi hay cần hỗ trợ thêm, vui lòng liên hệ với chúng tôi thông qua email hoặc số điện thoại 0946256271.</p>" +
                        "        <p style=\"margin: 20px 0; font-size: 16px;\">Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của HD_CARE. Chúc bạn một ngày tốt lành!</p>" +
                        "    </div>" +
                        "    <div style=\"background-color: #f1f1f1; text-align: center; padding: 10px; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;\">" +
                        "        <p style=\"margin: 0; font-size: 12px; color: #6c757d;\">&copy; 2024 HD_CARE. Tất cả các quyền được bảo lưu.</p>" +
                        "    </div>" +
                        "</div>",
                title, // %s: Tiêu đề
                savedAppointment.getPatient().getName(), // %s: Tên bệnh nhân
                isCancel ? "Lịch hẹn của bạn đã bị hủy." : "Lịch hẹn của bạn đã được xác nhận thành công.", // %s: Trạng thái lịch hẹn
                subjectTime, // %s: Thời gian hẹn
                appointmentDate, // %s: Ngày hẹn
                reason // %s: Lý do (nếu có)
        );

        emailServiceImpl.sendSimpleMessage(
                savedAppointment.getPatient().getEmail(),
                "Thông tin lịch hẹn từ HD_CARE",
                htmlContent,
                null
        );
    }


    @Async
    public void sendPrescription(AppointmentRequest appointmentRequest, byte[] data) throws MessagingException {
        String subject = "Đơn thuốc từ bác sĩ";

        String htmlContent = String.format(
                "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\">" +
                        "    <div style=\"background-color: #007bff; color: #fff; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;\">" +
                        "        <img src=\"https://res.cloudinary.com/dlokeyspj/image/upload/v1734713330/2024-12-20T23:48:46.7449537000.png\" alt=\"HD_CARE Logo\" style=\"width: 100px; margin-bottom: 10px;\">" +
                        "        <h1 style=\"margin: 0; font-size: 24px;\">Thông tin đơn thuốc</h1>" +
                        "    </div>" +
                        "    <div style=\"padding: 20px;\">" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Xin chào <strong>%s</strong>,</p>" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Chúng tôi gửi đến bạn đơn thuốc được kê bởi bác sĩ sau buổi khám. Vui lòng kiểm tra chi tiết trong tài liệu đính kèm.</p>" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Nếu bạn có bất kỳ câu hỏi hoặc cần hỗ trợ thêm, đừng ngần ngại liên hệ với chúng tôi qua email hoặc số điện thoại trong phần thông tin hỗ trợ.</p>" +
                        "        <p style=\"margin: 20px 0; font-size: 16px;\">Chúng tôi luôn sẵn sàng hỗ trợ bạn và chúc bạn nhanh chóng hồi phục sức khỏe!</p>" +
                        "    </div>" +
                        "    <div style=\"background-color: #f1f1f1; text-align: center; padding: 10px; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;\">" +
                        "        <p style=\"margin: 0; font-size: 12px; color: #6c757d;\">&copy; 2024 HD_CARE. Tất cả các quyền được bảo lưu.</p>" +
                        "    </div>" +
                        "</div>",
                appointmentRequest.getName() // %s: Tên bệnh nhân
        );

        emailServiceImpl.sendSimpleMessage(
                appointmentRequest.getEmail(),
                subject,
                htmlContent,
                data
        );
    }


    @Async
    public void sendReview(AppointmentRequest appointmentRequest) throws MessagingException {
        String subject = "Đánh giá bác sĩ: " + appointmentRequest.getNameDoctor();

        String htmlContent = String.format(
                "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\">" +
                        "    <div style=\"background-color: #007bff; color: #fff; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;\">" +
                        "        <img src=\"https://res.cloudinary.com/dlokeyspj/image/upload/v1734713330/2024-12-20T23:48:46.7449537000.png\" alt=\"HD_CARE Logo\" style=\"width: 100px; margin-bottom: 10px;\">" +
                        "        <h1 style=\"margin: 0; font-size: 24px;\">Cảm ơn bạn đã tin tưởng</h1>" +
                        "    </div>" +
                        "    <div style=\"padding: 20px;\">" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Kính gửi <strong>%s</strong>,</p>" + // Sửa đúng format
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Chúng tôi rất mong nhận được ý kiến đánh giá của bạn về dịch vụ và bác sĩ: <strong>%s</strong>. Những đánh giá của bạn là động lực để chúng tôi không ngừng cải thiện chất lượng dịch vụ.</p>" +
                        "        <div style=\"background-color: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0; text-align: center;\">" +
                        "            <p style=\"margin: 0; font-size: 16px;\">Vui lòng truy cập liên kết dưới đây để để lại đánh giá:</p>" +
                        "            <a href=\"http://localhost:3000/home?evaluate=1&idAppointment=%s\" style=\"display: inline-block; margin-top: 10px; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 5px; font-size: 16px;\">Đánh giá ngay</a>" +
                        "        </div>" +
                        "        <p style=\"margin: 20px 0; font-size: 16px;\">Chúng tôi rất trân trọng những đóng góp của bạn. Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua email hoặc số hotline được cung cấp trên website.</p>" +
                        "        <p style=\"margin: 20px 0; font-size: 16px;\">Cảm ơn bạn đã đồng hành cùng HD_CARE. Chúc bạn một ngày tốt lành!</p>" +
                        "    </div>" +
                        "    <div style=\"background-color: #f1f1f1; text-align: center; padding: 10px; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;\">" +
                        "        <p style=\"margin: 0; font-size: 12px; color: #6c757d;\">&copy; 2024 HD_CARE. Tất cả các quyền được bảo lưu.</p>" +
                        "    </div>" +
                        "</div>",
                appointmentRequest.getName(), // Tên bệnh nhân
                appointmentRequest.getNameDoctor(), // Tên bác sĩ
                appointmentRequest.getId() // ID lịch hẹn
        );
        emailServiceImpl.sendSimpleMessage(appointmentRequest.getEmail(), subject, htmlContent, null);
    }


    @Async
    public void sendActive(String token, OnRegisterEvent event) throws MessagingException {
        String activationUrl = String.format("http://localhost:8082/api/v1/auth/verify?token=%s", token);
        String subject = "Xác nhận tài khoản HD_CARE";

        String message = String.format(
                "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\">" +
                        "    <div style=\"background-color: #28a745; color: #fff; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;\">" +
                        "        <img src=\"https://res.cloudinary.com/dlokeyspj/image/upload/v1734713330/2024-12-20T23:48:46.7449537000.png\" alt=\"HD_CARE Logo\" style=\"width: 100px; margin-bottom: 10px;\">" +
                        "        <h1 style=\"margin: 0; font-size: 24px;\">Xác nhận tài khoản</h1>" +
                        "    </div>" +
                        "    <div style=\"padding: 20px;\">" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Kính gửi <strong>%s</strong>,</p>" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Chào mừng bạn đến với <strong>HD_CARE</strong>! Chúng tôi rất vui khi bạn đã đăng ký tài khoản.</p>" +
                        "        <p style=\"font-size: 16px; margin: 10px 0;\">Để kích hoạt tài khoản và bắt đầu trải nghiệm các dịch vụ của chúng tôi, vui lòng nhấp vào liên kết dưới đây:</p>" +
                        "        <div style=\"text-align: center; margin: 20px 0;\">" +
                        "            <a href=\"%s\" style=\"display: inline-block; padding: 10px 20px; background-color: #28a745; color: #fff; text-decoration: none; font-size: 16px; border-radius: 5px;\">Kích hoạt tài khoản</a>" +
                        "        </div>" +
                        "        <p style=\"margin: 20px 0; font-size: 16px;\">Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này hoặc liên hệ với chúng tôi để được hỗ trợ.</p>" +
                        "        <p style=\"margin: 20px 0; font-size: 16px;\">Chân thành cảm ơn,</p>" +
                        "        <p style=\"margin: 20px 0; font-size: 16px;\"><strong>Đội ngũ HD_CARE</strong></p>" +
                        "    </div>" +
                        "    <div style=\"background-color: #f1f1f1; text-align: center; padding: 10px; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;\">" +
                        "        <p style=\"margin: 0; font-size: 12px; color: #6c757d;\">&copy; 2024 HD_CARE. Tất cả các quyền được bảo lưu.</p>" +
                        "    </div>" +
                        "</div>",
                event.getUser().getName(), // Tên người dùng
                activationUrl // URL kích hoạt tài khoản
        );

        emailServiceImpl.sendSimpleMessage(event.getUser().getEmail(), subject, message, null);
    }

    public List<String> sendImage(List<MultipartFile> file2) {
        List<String> img = new ArrayList<>();
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

        for (int i = 0; i < file2.size(); i++) {
            String publicId = LocalDateTime.now().toString() + i;
            Map params1 = ObjectUtils.asMap(
                    "public_id", publicId,
                    "use_filename", true,
                    "unique_filename", false
            );
            try {// Upload file lên Cloudinary và trả về URL
                Map uploadResult = cloudinary.uploader().upload(file2.get(i).getBytes(), params1);
                img.add(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                return null;  // Có thể xử lý lỗi ở đây
            }
        };

        return img;
    }

}
