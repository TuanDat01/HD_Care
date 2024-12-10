package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.*;
import com.doctorcare.PD_project.dto.response.ManagePatient;
import com.doctorcare.PD_project.entity.*;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.AppointmentMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.AppointmentRepository;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Lazy
public class AppointmentService {
    ScheduleService scheduleService;
    DoctorService doctorService;
    AppointmentMapper appointmentMapper;
    AppointmentRepository appointmentRepository;
    UserMapper userMapper;
    PatientService patientService;
    SendEmailService emailService;
    PrescriptionService prescriptionService;
    DoctorRepository doctorRepository;

    public List<AppointmentRequest> changeToListRequest(List<Appointment> appointments) {
        return appointments.stream().map(appointmentMapper::toAppointmentRequest).toList();
    }

    public AppointmentV2Request getInfo(String idPatient, String idDoctor, String idSchedule) throws AppException {
        Patient patient1 = patientService.getPatientById(idPatient);
        //String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //Patient patient1 = patientService.getPatientByUsername()
        AppointmentV2Request appointmentV2Request = new AppointmentV2Request();
        PatientRequest patientRequest = userMapper.tPatientRequest(patient1);
        patientRequest.setId(idPatient); //context holder xu ly
        appointmentV2Request.setPatientRequest(patientRequest);
        appointmentV2Request.setDoctorScheduleRequest(scheduleService.getInfoSchedule(idSchedule,idDoctor));
        return appointmentV2Request;
    }

    @Transactional
    public AppointmentRequest createAppointment(AppointmentRequest appointmentRequest) throws AppException, MessagingException {

        Patient patient = patientService.updatePatient(appointmentRequest); //new

        // Kiểm tra xem thời gian đặt lịch hợp lệ hay không
        Schedule schedule = scheduleService.getScheduleById(appointmentRequest.getScheduleId());

        LocalDateTime localDateTime = LocalDateTime.now();

        if (!schedule.getStart().isAfter(LocalDateTime.now()))
            throw new AppException(ErrorCode.SCHEDULE_INVALID);

        Appointment appointment = appointmentMapper.toAppointment(appointmentRequest);
        Doctor doctor = doctorService.findDoctorBySchedules(schedule.getId());

        Prescription prescription = new Prescription();

        appointment.setPatient(patient);
        appointment.setSchedule(schedule);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.PENDING.toString());
        appointment.setPrescription(prescription);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        //        emailService.sendAppointmentConfirmation(savedAppointment);
        return appointmentMapper.toAppointmentRequest(savedAppointment);
    }

    public Map<String,String> convertDate(String date, String month){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String,String> dat1e = new HashMap<>();
        LocalDate start, end;
         if (month != null) {
            LocalDate givenMonth = LocalDate.parse(month,formatter);
            YearMonth yearMonth = YearMonth.from(givenMonth);
            // Lấy ngày bắt đầu và kết thúc của tháng
            start = yearMonth.atDay(1);
            end = yearMonth.atEndOfMonth();
        } else if (date != null) {
            LocalDate givenWeek = LocalDate.parse(date, formatter);
            // Lấy ngày bắt đầu và kết thúc tuần
            start = givenWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            end = givenWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        } else {
            // Nếu cả date và month đều null
            System.out.println("Date or month must be provided.");
            return null;
        }
        dat1e.put("start",start.toString());
        dat1e.put("end",end.toString());
        return dat1e;
    }

    public Page<AppointmentRequest> findAllByPatientId(String id,String date, String week, String month, String status, int page) {
        Map<String,String> date1 = null;
        Pageable pageable = PageRequest.of(page,5);
        Page<Appointment> appointmentList;
        if (date == null && week == null && month == null) {
            appointmentList =  appointmentRepository.findAllByPatientId(id, null, null, status, pageable);
        } else if (week != null || month != null) {
            date1 = convertDate(week,month);
            appointmentList =  appointmentRepository.findAllByPatientId(id, date1!=null?date1.get("start"):null, date1!=null?date1.get("end"):null,status,pageable);
        } else {
            appointmentList =  appointmentRepository.findAllByPatientId(id, date, date, status, pageable);
        }
        System.out.println(appointmentList);
        return appointmentList.map(appointmentMapper::toAppointmentRequest);
    }

    public List<Appointment> getAppointBySchedule(Schedule schedule){
        return appointmentRepository.findAppointmentBySchedule(schedule);
    }
    public AppointmentRequest getAppointmentById(String id) throws AppException {
        return appointmentMapper.toAppointmentRequest(appointmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_APPOINTMENT)));
    }

    public Page<AppointmentRequest> getAppointmentByDoctor(String id, String date, String status, Integer page,String name) throws AppException {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointmentList =  appointmentRepository.findByDoctor(id, date, status,name, pageable);
        return appointmentList.map(appointmentMapper::toAppointmentRequest);
    }

    @Transactional
    public Appointment getAppointmentByDoctorAndId(String id, UpdateStatusAppointment updateStatusAppointment) throws AppException { //idDoctor
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_APPOINTMENT));
        if (!appointment.getDoctor().getId().equals(updateStatusAppointment.getIdDoctor())) {
            throw new AppException(ErrorCode.NOT_FOUND_DOCTOR);
        }
        appointment.setStatus(updateStatusAppointment.getStatus());
        appointment.setNote(updateStatusAppointment.getNote());
        System.out.println(appointment);
        return appointment;
    }

    public Page<ManagePatient> getPatientOfDoctor (String id, Integer page, String keyword) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository.getPatientOfDoctor(id, keyword, pageable);
    }

    public AppointmentRequest getData(String appointmentId) throws AppException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return appointmentMapper.toAppointmentRequest(appointment);
    }

    public void createPdf(String appointmentId, ByteArrayOutputStream outputStream) throws AppException {
        AppointmentRequest appointmentRequest = getData(appointmentId);
        Patient patient = patientService.getPatientById(appointmentRequest.getIdPatient());
        Doctor doctor = doctorRepository.findById(appointmentRequest.getIdDoctor())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));

        List<Medicine> medicines = prescriptionService.getMedicineByPrescription(appointmentRequest.getPrescriptionId());
        System.out.println(medicines);

        Prescription prescription = prescriptionService.getPrescriptionById(appointmentRequest.getPrescriptionId());
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Cài đặt font hỗ trợ tiếng Việt
            BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(baseFont, 18, Font.BOLD);
            Font fontHeader = new Font(baseFont, 12, Font.BOLD);
            Font fontBody = new Font(baseFont, 12);

            Image logo = Image.getInstance("src/main/resources/images/logo.png");
            logo.scaleToFit(50, 50);
            // Tạo bảng với 2 cột (một cột cho thông tin bác sĩ, cột còn lại cho logo)
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100); // Đảm bảo bảng chiếm toàn bộ chiều rộng trang

            // Cài đặt độ rộng của các cột, cột trái rộng hơn để chứa thông tin
            float[] columnWidths = { 70f, 30f }; // 70% cho thông tin, 30% cho logo
            infoTable.setWidths(columnWidths);

            String clinicInfo = "Tên phòng khám: " + doctor.getClinicName() + "\n" +
                    "Địa chỉ: " + doctor.getDistrict() + " - " + doctor.getCity() + "\n" +
                    "Email: " + doctor.getEmail() + "\n" +
                    "Điện thoại: " + doctor.getPhone();

            // Tạo các ô chứa thông tin bác sĩ
            PdfPCell clinicInfoCell = new PdfPCell(new Paragraph(clinicInfo, fontBody));
            clinicInfoCell.setBorder(Rectangle.NO_BORDER); // Không viền cho ô
            clinicInfoCell.setHorizontalAlignment(Element.ALIGN_LEFT);  // Căn trái
            clinicInfoCell.setVerticalAlignment(Element.ALIGN_TOP);    // Căn trên

            // Thêm các ô thông tin vào bảng
            infoTable.addCell(clinicInfoCell);

            // Tạo ô cho logo (cột thứ hai)
            PdfPCell logoCell = new PdfPCell(logo); // Thêm logo vào ô
            logoCell.setBorder(Rectangle.NO_BORDER); // Không viền
            logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);  // Căn phải
            logoCell.setVerticalAlignment(Element.ALIGN_TOP);    // Căn trên
            infoTable.addCell(logoCell);  // Thêm logo vào cột 2

            // Thêm bảng vào tài liệu
            document.add(infoTable);

            // Tiêu đề chính của tài liệu
            Paragraph titleParagraph = new Paragraph("ĐƠN THUỐC", fontTitle);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingAfter(20);
            document.add(titleParagraph);

            // Danh sách thông tin bệnh nhân
            Paragraph patientInfoParagraph = new Paragraph();
            patientInfoParagraph.setSpacingAfter(20);

            // Thêm thông tin bệnh nhân theo từng dòng với khoảng cách giữa hai mục
            patientInfoParagraph.add(new Chunk("Tên bệnh nhân: " + patient.getName(), fontBody));
            patientInfoParagraph.add(new Chunk(new VerticalPositionMark())); // thêm vị trí dọc nếu cần
            patientInfoParagraph.add(new Chunk("Năm sinh: " + patient.getDob().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), fontBody));
            patientInfoParagraph.add(Chunk.NEWLINE);

            patientInfoParagraph.add(new Chunk("Giới tính: " + patient.getGender(), fontBody));
            patientInfoParagraph.add(new Chunk(new VerticalPositionMark())); // thêm vị trí dọc nếu cần

            patientInfoParagraph.add(new Chunk("Ngày khám: " + appointmentRequest.getStart(), fontBody));
            patientInfoParagraph.add(Chunk.NEWLINE);

            patientInfoParagraph.add(new Chunk("Lý do khám: " + appointmentRequest.getDescription(), fontBody));
            patientInfoParagraph.add(new Chunk(new VerticalPositionMark())); // thêm vị trí dọc nếu cần

            patientInfoParagraph.add(new Chunk("Chẩn đoán: " + prescription.getResult(), fontBody));

            // Thêm đoạn văn bản vào tài liệu
            document.add(patientInfoParagraph);

            // Tạo bảng với 6 cột
            PdfPTable medicineTable = new PdfPTable(6);
            medicineTable.setWidthPercentage(100);
            medicineTable.setSpacingBefore(10);
            medicineTable.setSpacingAfter(10);

            // Thiết lập chiều rộng cho từng cột
            float[] medicineColumnWidths = { 10f, 25f, 10f, 15f, 30f, 30f }; // Tổng các tỷ lệ = 100f
            medicineTable.setWidths(medicineColumnWidths);

            // Tiêu đề các cột (sử dụng font tiếng Việt)
            String[] medicineHeaders = {"STT", "Tên thuốc", "Dạng", "Số lượng", "Hướng dẫn", "Lưu ý"};
            for (String header : medicineHeaders) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header, fontHeader));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);  // Căn giữa theo chiều ngang
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);    // Căn giữa theo chiều dọc
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell.setFixedHeight(35f);
                headerCell.setPaddingTop(5f);
                headerCell.setPaddingBottom(10f);
                medicineTable.addCell(headerCell);
            }

            // Thêm dữ liệu vào bảng
            for (int i = 0; i < medicines.size(); i++) {
                PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(i + 1), fontBody));
                PdfPCell cell2 = new PdfPCell(new Paragraph(medicines.get(i).getName(), fontBody));
                PdfPCell cell3 = new PdfPCell(new Paragraph(medicines.get(i).getMedicineType(), fontBody));
                PdfPCell cell4 = new PdfPCell(new Paragraph(medicines.get(i).getQuantity(), fontBody));
                PdfPCell cell5 = new PdfPCell(new Paragraph(medicines.get(i).getInstruction(), fontBody));
                PdfPCell cell6 = new PdfPCell(new Paragraph(medicines.get(i).getNote(), fontBody));

                // Căn giữa nội dung theo chiều dọc và chiều ngang
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // Thêm padding cho các ô
                cell1.setPaddingBottom(15f);
                cell2.setPaddingBottom(15f);
                cell3.setPaddingBottom(15f);
                cell4.setPaddingBottom(15f);
                cell5.setPaddingBottom(15f);
                cell6.setPaddingBottom(15f);

                cell1.setPaddingTop(10f);
                cell2.setPaddingTop(10f);
                cell3.setPaddingTop(10f);
                cell4.setPaddingTop(10f);
                cell5.setPaddingTop(10f);
                cell6.setPaddingTop(10f);

                cell1.setPaddingRight(5f);
                cell2.setPaddingRight(5f);
                cell3.setPaddingRight(5f);
                cell4.setPaddingRight(5f);
                cell5.setPaddingRight(5f);
                cell6.setPaddingRight(5f);

                cell1.setPaddingLeft(5f);
                cell2.setPaddingLeft(5f);
                cell3.setPaddingLeft(5f);
                cell4.setPaddingLeft(5f);
                cell5.setPaddingLeft(5f);
                cell6.setPaddingLeft(5f);

                medicineTable.addCell(cell1);
                medicineTable.addCell(cell2);
                medicineTable.addCell(cell3);
                medicineTable.addCell(cell4);
                medicineTable.addCell(cell5);
                medicineTable.addCell(cell6);
            }

            // Thêm bảng thuốc vào tài liệu
            document.add(medicineTable);

            Paragraph footer = new Paragraph("Ngày kê đơn: " + prescription.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")) + "\n\n\n\nBác sĩ: " + appointmentRequest.getNameDoctor(), fontBody);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);
            // Thêm bảng vào tài liệu
            document.close();

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi tạo PDF: " + e.getMessage(), e);
        }
    }


    public Page<AppointmentRequest> filterAppointment(String id, String date,String month, String status, Integer page, String name) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        try {
            Map<String,String> date1 = convertDate(date, month);
            Page<Appointment> appointmentPage = appointmentRepository
                    .filterAppointment(id,
                            date1 != null ? date1.get("start") : null,
                            date1 != null ? date1.get("end") : null,
                            status,
                            name,
                            pageable);

            return appointmentPage.map(appointmentMapper::toAppointmentRequest);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date or month format: " + e.getMessage());
        }

        return Page.empty(pageable);
    }

}
