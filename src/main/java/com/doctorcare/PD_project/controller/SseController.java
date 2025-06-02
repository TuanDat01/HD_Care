package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.service.AuthenticationService;
import com.doctorcare.PD_project.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/sse/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<NotificationMessage>> subscribe(@RequestParam("token") String token) throws ParseException {
        System.out.println("inn");
        return sseService.subscribe(token);
    }

}

