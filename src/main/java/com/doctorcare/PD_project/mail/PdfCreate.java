//package com.doctorcare.PD_project.mail;
//
//import com.doctorcare.PD_project.dto.request.AppointmentRequest;
//import com.doctorcare.PD_project.entity.MedicineDetail;
//import com.doctorcare.PD_project.entity.Patient;
//import com.doctorcare.PD_project.entity.Prescription;
//import com.doctorcare.PD_project.enums.ErrorCode;
//import com.doctorcare.PD_project.exception.AppException;
//import com.doctorcare.PD_project.responsitory.PatientRepository;
//import com.doctorcare.PD_project.service.PatientService;
//import com.doctorcare.PD_project.service.PrescriptionService;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.time.format.DateTimeFormatter;
//
//@RequiredArgsConstructor
//@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
//public class PdfCreate {
//    PrescriptionService prescriptionService;
//    PatientService patientService;
//    public void createPdf(AppointmentRequest appointmentRequest, ByteArrayOutputStream outputStream) throws FileNotFoundException, DocumentException, IOException, AppException {
//        Patient patient = patientService.getPatientById(appointmentRequest.getId());
//        java.util.List<MedicineDetail> medicineDetails = prescriptionService.getMedicineByPrescription(appointmentRequest.getPrescriptionId());
//        Prescription prescription = prescriptionService.getPrescriptionById(appointmentRequest.getPrescriptionId());
//        Document document = new Document();
//        try {
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//
//            // Cài đặt font hỗ trợ tiếng Việt
//            BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Font fontTitle = new Font(baseFont, 16, Font.BOLD);
//            Font fontHeader = new Font(baseFont, 12, Font.BOLD);
//            Font fontBody = new Font(baseFont, 12);
//
//            // Tiêu đề chính của tài liệu
//            Paragraph paragraph1 = new Paragraph("Đơn thuốc", fontTitle);
//            paragraph1.setAlignment(Element.ALIGN_CENTER);
//            paragraph1.setSpacingAfter(20);
//            document.add(paragraph1);
//
//            // Danh sách thông tin bệnh nhân
//            List patientInfoList = new List(List.UNORDERED);
//            patientInfoList.add(new ListItem("Tên bệnh nhân: " + patient.getName(), fontBody));
//            patientInfoList.add(new ListItem("Năm sinh: 1985" + patient.getDob().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), fontBody));
//            patientInfoList.add(new ListItem("Chẩn đoán: " + prescription.getResult() , fontBody));
//
//            // Thêm danh sách vào tài liệu
//            Paragraph paragraph2 = new Paragraph();
//            paragraph2.add(patientInfoList);
//            paragraph2.setSpacingAfter(20);
//            document.add(paragraph2);
//
//            // Tạo bảng với 6 cột
//            PdfPTable table = new PdfPTable(6);
//            table.setWidthPercentage(100);
//            table.setSpacingBefore(10);
//            table.setSpacingAfter(10);
//
//            // Tiêu đề các cột (sử dụng font tiếng Việt)
//            String[] headers = {"STT", "Tên thuốc", "Dạng thuốc", "Số lượng", "Hướng dẫn", "Lưu ý"};
//            for (String header : headers) {
//                PdfPCell cell = new PdfPCell(new Paragraph(header, fontHeader));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                table.addCell(cell);
//            }
//
//            // Thêm dữ liệu vào bảng
//            for (int i = 0; i <= medicineDetails.size(); i++) { // Giả sử có 5 dòng dữ liệu
//                table.addCell(new PdfPCell(new Paragraph(String.valueOf(i+1), fontBody)));
//                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getName(), fontBody)));
//                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getMedicineType(), fontBody)));
//                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getQuantity(), fontBody)));
//                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getInstruction(), fontBody)));
//                table.addCell(new PdfPCell(new Paragraph(medicineDetails.get(i).getNote(), fontBody)));
//            }
//
//            // Thêm bảng vào tài liệu
//            document.add(table);
//            document.close();
//
//            System.out.println("PDF đã được tạo thành công!");
//        } catch (DocumentException | IOException e) {
//            throw new RuntimeException("Có lỗi xảy ra khi tạo PDF: " + e.getMessage(), e);
//        }
//    }
//}
