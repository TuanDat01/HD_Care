package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.AppointmentV2Request;
import com.doctorcare.PD_project.dto.request.PatientRequest;
import com.doctorcare.PD_project.dto.request.UpdateStatusAppointment;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.*;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.AppointmentMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.AppointmentRepository;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.PatientRepository;
import com.doctorcare.PD_project.responsitory.ScheduleRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AppointmentService {
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;
    ScheduleRepository scheduleRepository;
    AppointmentMapper appointmentMapper;
    AppointmentRepository appointmentRepository;
    UserMapper userMapper;
    PatientService patientService;
    SendEmailService emailService;
    PrescriptionService prescriptionService;

    public AppointmentV2Request getInfo(String idPatient, String idDoctor, String idSchedule){
        Patient patient1 = patientRepository.findById(idPatient).orElseThrow(()-> new RuntimeException("not found patient"));
        AppointmentV2Request appointmentV2Request = new AppointmentV2Request();
        PatientRequest patientRequest = userMapper.tPatientRequest(patient1);
        patientRequest.setId(idPatient);
        appointmentV2Request.setPatientRequest(patientRequest);
        appointmentV2Request.setDoctorScheduleRequest(scheduleRepository.getInfoSchedule(idSchedule,idDoctor));
        return appointmentV2Request;
    }

    @Transactional
    public AppointmentRequest createAppointment(AppointmentRequest appointmentRequest) throws AppException {
        Patient patient = patientService.updatePatient(appointmentRequest);
        patientRepository.save(patient);
        Schedule schedule = scheduleRepository.findById(appointmentRequest.getScheduleId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_SCHEDULE));
        Appointment appointment = appointmentMapper.toAppointment(appointmentRequest);
        System.out.println(schedule.getId());
        Doctor doctor = doctorRepository.findDoctorBySchedules(schedule.getId());
        Prescription prescription = new Prescription();
        appointment.setPatient(patient);
        appointment.setSchedule(schedule);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.PENDING.toString());
        appointment.setPrescription(prescription);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        AppointmentRequest savedAppointmentRequest = appointmentMapper.toAppointmentRequest(savedAppointment);
        savedAppointmentRequest.setNameDoctor(doctor.getName());
        //emailService.sendAppointmentConfirmation(savedAppointment);
        return savedAppointmentRequest;
    }


    public List<AppointmentRequest> findAllByPatientId(String id) {
        List<Appointment> appointmentList =  appointmentRepository.findAllByPatientId(id);
        System.out.println(appointmentList);
        List<Appointment> appointmentsFilter = appointmentList.stream().peek(appointment -> {
            if (appointment.getSchedule().getEnd().isBefore(LocalDateTime.now())) {
                appointment.setStatus(AppointmentStatus.PENDING.toString());
                appointmentRepository.save(appointment);
            }
        }).toList();
        System.out.println(appointmentsFilter);
        return appointmentsFilter.stream().map(appointmentMapper::toAppointmentRequest).toList();
    }
    public AppointmentRequest getAppointmentById(String id) throws AppException {
        return appointmentMapper.toAppointmentRequest(appointmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_APPOINTMENT)));
    }
    public List<AppointmentRequest> getAppointmentByDoctor(String id,String date,String status) {
        List<Appointment> appointmentList =  appointmentRepository.findByDoctor(id,date,status);
        List<Appointment> appointmentsFilter = appointmentList.stream().peek(appointment -> {
            if(appointment.getSchedule().getEnd().isBefore(LocalDateTime.now())) {
                appointment.setStatus(AppointmentStatus.COMPLETED.toString());
                appointmentRepository.save(appointment);
            };
        }).toList();
        return appointmentsFilter.stream().map(appointmentMapper::toAppointmentRequest).toList();
    }
    @Transactional
    public AppointmentRequest getAppointmentByDoctorAndId(String id, UpdateStatusAppointment updateStatusAppointment) throws AppException { //idDoctor
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_APPOINTMENT));
        if (!appointment.getDoctor().getId().equals(updateStatusAppointment.getIdDoctor())) {
            throw new AppException(ErrorCode.NOT_FOUND_DOCTOR);
        }
        appointment.setStatus(updateStatusAppointment.getStatus());
        return appointmentMapper.toAppointmentRequest(appointment);
    }

//    public List<AppointmentRequest> findAppointment(String idDoctor,String id) {
//        Appointment appointment = appointmentRepository.findAllByDoctorId(idDoctor);
//    }
    public void createPdf(AppointmentRequest appointmentRequest, ByteArrayOutputStream outputStream) throws AppException {
        Patient patient = patientService.getPatientById(appointmentRequest.getIdPatient());
        List<MedicineDetail> medicineDetails = prescriptionService.getMedicineByPrescription(appointmentRequest.getPrescriptionId());
        System.out.println(medicineDetails);
        Prescription prescription = prescriptionService.getPrescriptionById(appointmentRequest.getPrescriptionId());
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Cài đặt font hỗ trợ tiếng Việt
            BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(baseFont, 16, Font.BOLD);
            Font fontHeader = new Font(baseFont, 12, Font.BOLD);
            Font fontBody = new Font(baseFont, 12);

            // Tiêu đề chính của tài liệu
            Paragraph paragraph1 = new Paragraph("Đơn thuốc", fontTitle);
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            paragraph1.setSpacingAfter(20);
            document.add(paragraph1);

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
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Tiêu đề các cột (sử dụng font tiếng Việt)
            String[] headers = {"STT", "Tên thuốc", "Dạng thuốc", "Số lượng", "Hướng dẫn", "Lưu ý"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, fontHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Thêm dữ liệu vào bảng
            for (int i = 0; i < medicineDetails.size(); i++) { // Giả sử có 5 dòng dữ liệu
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(i+1), fontBody)));
                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getName(), fontBody)));
                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getMedicineType(), fontBody)));
                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getQuantity(), fontBody)));
                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getInstruction(), fontBody)));
                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getNote(), fontBody)));
            }
            Paragraph footer = new Paragraph("Ngày kê đơn: " + prescription.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yy hh:mm")) + "\n\n\n\nBác sĩ: " + appointmentRequest.getNameDoctor(), fontBody);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(table);
            document.add(footer);
            // Thêm bảng vào tài liệu
            document.close();

            System.out.println("PDF đã được tạo thành công!");
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Có lỗi xảy ra khi tạo PDF: " + e.getMessage(), e);
        }
    }

    public List<AppointmentRequest> filterAppointment(String id, String date,String month) {
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            // Parse the string into a Date object
//            Date endDate = formatter.parse(date);
//            Date startDate = new Date();
//            startDate.setDate(endDate.getDate() - endDate.getDay() +1);
//            LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            System.out.println(start);
//            System.out.println(end);
//            List<Appointment> appointmentList= appointmentRepository.filterAppointment(id,start.toString(),end.toString());
//            return appointmentList.stream().map(appointmentMapper::toAppointmentRequest).toList();
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start, end;

        try {
            if (date != null) {
                LocalDate givenWeek = LocalDate.parse(date, formatter);
                // Lấy ngày bắt đầu và kết thúc tuần
                start = givenWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                end = givenWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            } else if (month != null) {
                LocalDate givenMonth = LocalDate.parse(month,formatter);
                YearMonth yearMonth = YearMonth.from(givenMonth);
                // Lấy ngày bắt đầu và kết thúc của tháng
                start = yearMonth.atDay(1);
                end = yearMonth.atEndOfMonth();
            } else {
                // Nếu cả date và month đều null
                System.out.println("Date or month must be provided.");
                return List.of(); // Hoặc xử lý lỗi theo yêu cầu
            }
            List<Appointment> appointmentList = appointmentRepository.filterAppointment(id, start.toString(), end.toString());
            return appointmentList.stream().map(appointmentMapper::toAppointmentRequest).toList();

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date or month format: " + e.getMessage());
        }
        return List.of();
    }
    }
