package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.dto.response.BasicInfoUserResponse;
import com.doctorcare.PD_project.dto.response.NotificateDTO;
import com.doctorcare.PD_project.entity.Notification;
import com.doctorcare.PD_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface NotificateMap {
    NotificateDTO toNotificateDTO(HasUser hasUser);
    @Mapping(source = "receiver.username", target = "username")
    NotificationMessage toNotificationMessage(Notification notification);

}
