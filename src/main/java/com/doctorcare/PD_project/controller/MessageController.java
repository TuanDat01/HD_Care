package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.HistoryRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.MessageResponse;
import com.doctorcare.PD_project.entity.HistoryMessage;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class MessageController {
    MessageService messageService;

    @PostMapping
    public ApiResponse addMessage(@RequestBody List<HistoryRequest> historyRequest) throws AppException {
        messageService.addMessage(historyRequest);
        return ApiResponse
                .builder()
                .message("Add successful")
                .build();
    }

    @GetMapping ApiResponse<List<MessageResponse>> getMessage(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam String sender,
                                                              @RequestParam String receiver)
    {
        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.getMessage(page,sender,receiver))
                .build();
    }
}
