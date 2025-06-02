package com.doctorcare.PD_project.enums;

import lombok.Data;
import lombok.Getter;

import java.util.List;
@Getter
public enum NotificationType {
    LIKE_POST("Yêu thích", "{user} đã thích bài viết của bạn", "{time}"),
    COMMENT_POST("Bình luận", "{user} đã bình luận bài viết của bạn","{time}"),
    LIKE_COMMENT("Thích bình luận", "{user} đã thích bình luận của bạn","{time}"),
    COMMENT_COMMENT("Bình luận", "{user} đã bình luận bình luận của bạn","{time}"),
    FOLLOW("Theo dõi", "{user} đã theo dõi bạn","{time}"),
    MAKE_APPOINTMENT("Đặt lịch khám", "Bạn đặt lịch thành công với bác sĩ {doctor} vào lúc {date}","{time}"),
    CHANGE_STATUS("Thay đổi lịch", "{doctor} đã thay đổi trạng thái lịch hẹn thành {status}","{time}");
    public static final List<NotificationType> POST = List.of(
            LIKE_POST
    );
    public static final List<NotificationType> COMMENT_FOLLOW = List.of(
            LIKE_COMMENT,
            COMMENT_COMMENT,
            COMMENT_POST,

            FOLLOW
    );
    private String type;
    private String message;
    private String time;
    NotificationType(String type, String message, String time) {
        this.type = type;
        this.message = message;
        this.time = time;
    }
}
