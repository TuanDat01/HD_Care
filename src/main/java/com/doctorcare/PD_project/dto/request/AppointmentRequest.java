package com.doctorcare.PD_project.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRequest {

    String id;

    @NotBlank(message = "ID bệnh nhân không được để trống.")
    String idPatient;

    @NotBlank(message = "Tên bệnh nhân không được để trống.")
    @Size(max = 100, message = "Tên bệnh nhân không được vượt quá 100 ký tự.")
    String name;

    @NotBlank(message = "GENDER_NOT_BLANK")
    String gender;

    @NotBlank(message = "Địa chỉ không được để trống.")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự.")
    String address;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự.")
    String description;

    @NotBlank(message = "Tiêu đề không được để trống.")
    @Size(max = 150, message = "Tiêu đề không được vượt quá 150 ký tự.")
    String title;

    @NotBlank(message = "ID lịch trình không được để trống.")
    String scheduleId;

    @NotBlank(message = "Email không được để trống.")
    @Email(message = "Email không hợp lệ.")
    String email;

    String status;

    String start;

    String end;


//    @NotNull(message = "DOB_NOT_BLANK")
//    @Past(message = "Ngày sinh phải là ngày trong quá khứ.")
    LocalDate dob;

    String prescriptionId;

    String idDoctor;

    String nameDoctor;

    String result;

    String img;
}
