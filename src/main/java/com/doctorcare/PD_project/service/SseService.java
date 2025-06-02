package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SseService {

    private final Map<String, FluxSink<ServerSentEvent<NotificationMessage>>> clients = new ConcurrentHashMap<>();
    private final AuthenticationService authenticationService;

    public Mono<Void> sendToUser(String username, NotificationMessage message) {
        FluxSink<ServerSentEvent<NotificationMessage>> sink = clients.get(username);
        System.out.println("sink follow : " + sink);
        if (sink != null) {
            System.out.println("has user");
            System.out.println("Sending message: " + message.getData());
            sink.next(ServerSentEvent.builder(message).build());
        }
        return Mono.empty();
    }

    public Flux<ServerSentEvent<NotificationMessage>> subscribe(String token) throws ParseException {
        String name = authenticationService.extractToken(token).getSubject();

        return Flux.<ServerSentEvent<NotificationMessage>>create(sink -> {
            clients.put(name, sink);
            System.out.println("establish sink : " + clients);
            sink.next(ServerSentEvent.<NotificationMessage>builder()
                    .comment("Connected to SSE successfully")
                    .build());
        }).share();
    }
}
