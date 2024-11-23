package com.doctorcare.PD_project.event.listen;

import com.doctorcare.PD_project.entity.VerifyToken;
import com.doctorcare.PD_project.event.create.OnRegisterEvent;
import com.doctorcare.PD_project.mail.EmailService;
import com.doctorcare.PD_project.service.SendEmailService;
import com.doctorcare.PD_project.service.VerifyTokenService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserListen {
    SendEmailService sendEmailService;
    VerifyTokenService verifyTokenService;
    @EventListener
    public void handleOnRegisterEvent(OnRegisterEvent event) throws MessagingException {
        String token = UUID.randomUUID().toString();
        VerifyToken verifyToken1 = new VerifyToken(token,event.getUser());
        System.out.println(event.getUser());
        verifyTokenService.saveVerifyToken(verifyToken1);
        sendEmailService.sendActive(token, event);

    }
}
