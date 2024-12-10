package com.doctorcare.PD_project.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_KEY(1001, "Đã có lỗi xảy ra", HttpStatus.BAD_REQUEST),
    FULL_NAME_NOT_BLANK(1002, "Họ và tên không được để trống", HttpStatus.BAD_REQUEST),
    FULL_NAME_SIZE(1003, "Họ và tên phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1004, "Số điện thoại đã tồn tại", HttpStatus.CONFLICT),
    PHONE_NUMBER_NOT_BLANK(1005, "Số điện thoại không được để trống", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_PATTERN(1006, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1007, "Email đã tồn tại", HttpStatus.CONFLICT),
    EMAIL_NOT_BLANK(1008, "Email không được để trống", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1009, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(1010, "Tên đăng nhập đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_BLANK(1011, "Tên đăng nhập không được để trống", HttpStatus.BAD_REQUEST),
    USERNAME_SIZE(1012, "Tên đăng nhập phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    USERNAME_PATTERN(1013, "Tên đăng nhập không hợp lệ", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_BLANK(1014, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    PASSWORD_SIZE(1015, "Mật khẩu phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_PATTERN(1016, "Mật khẩu phải chứa ít nhất một chữ số, một ký tự thường, một ký tự hoa và một ký tự đặc biệt", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1017,"No authenticated" , HttpStatus.UNAUTHORIZED ),
    DATE_INVALID(1017, "Lịch hẹn không hợp lệ",HttpStatus.BAD_REQUEST),
    TIME_INVALID(1018, "Giờ bắt đầu phải trước giờ kết thúc",HttpStatus.BAD_REQUEST),
    NOT_FOUND_SCHEDULE(1019, "Không tìm thấy lịch hẹn",HttpStatus.NOT_FOUND),
    NOT_FOUND_DOCTOR(1020, "Không tìm thấy bác sĩ",HttpStatus.NOT_FOUND),
    NOT_FOUND_PATIENT(1021, "Không tìm thấy bệnh nhân",HttpStatus.NOT_FOUND),
    NOT_FOUND_APPOINTMENT(1022, "Không tìm thấy cuộc hẹn",HttpStatus.NOT_FOUND),
    NOT_FOUND_MEDICINE(1023, "Không tìm thấy thuốc",HttpStatus.NOT_FOUND),
    NOT_FOUND_PRESCRIPTION(1024, "Không tìm thấy đơn thuốc",HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(1025, "Email đã tồn tại", HttpStatus.CONFLICT),
    INVALID_FORMAT(1030, "Định dạng email không hợp lệ", HttpStatus.CONFLICT),
    TOKEN_EXPIRED(1026, "Token expired", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1033, "you do not permission to access", HttpStatus.FORBIDDEN),
    PASSWORD_EXIST(1027, "Mật khẩu đã tồn tại", HttpStatus.BAD_REQUEST),
    NO_ACTIVE(1028,"Vui lòng kiểm tra email để kích hoạt tài khoản" ,HttpStatus.OK),
    UPDATE_STATUS(1029, "Vui lòng cập nhật trạng thái là đã xác nhận cho đơn thuốc", HttpStatus.BAD_REQUEST),
    PAGE_VALID(1030,"Giá trị của trang không hợp lệ" ,HttpStatus.BAD_REQUEST ),
    START_TIME_EXISTED(1031,"Giờ bắt đầu đã tồn tại" , HttpStatus.BAD_REQUEST),
    DURATION_TIME(1032,"Khoảng thời gian từ giờ bắt đầu đến giờ kết thúc phải là 1 giờ", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIAL(1033, "Tài khoản hoặc mật khẩu không đúng", HttpStatus.BAD_REQUEST),
    SCHEDULE_INVALID(1034, "Thời gian khám phải bắt đầu sau thời gian hiện tại", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_FOUND(1035, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    DOB_NOT_BLANK(1036, "Vui lòng cập nhật ngày sinh ở trang cá nhân", HttpStatus.BAD_REQUEST),
    GENDER_NOT_BLANK(1037, "Giới tính không được để trống", HttpStatus.BAD_REQUEST),;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private final int code;
    private final String message;
    private final HttpStatus status;
}
