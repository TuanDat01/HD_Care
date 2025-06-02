package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.HistoryRequest;
import com.doctorcare.PD_project.dto.response.MessageResponse;
import com.doctorcare.PD_project.entity.HistoryMessage;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.ChatMapper;
import com.doctorcare.PD_project.respository.MessageRepository;
import com.doctorcare.PD_project.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class MessageService {
    MessageRepository messageRepository;
    UserRepository userRepository;
    ChatMapper chatMapper;

    public void addMessage(List<HistoryRequest> requests) throws AppException {
        List<HistoryMessage> list = new ArrayList<>();

        for (HistoryRequest historyRequest : requests) {
            User receiver = userRepository.findByUsername(historyRequest.getReceiver())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER));

            User sender = userRepository.findByUsername(historyRequest.getSender())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER));

            HistoryMessage historyMessage = chatMapper.toHistoryMessage(historyRequest);
            historyMessage.setReceiver(receiver);
            historyMessage.setSender(sender);
            System.out.println(historyMessage.toString());
            list.add(historyMessage);
        }

        messageRepository.saveAll(list);
    }

    public List<MessageResponse> getMessage(int page, String sender, String receiver) {
        Pageable pageable;

        if (page == 0) {
            long totalMessages = messageRepository.countBySenderUsernameAndReceiverId(sender, receiver);
            int pageSize = 5;
            int totalPages = (int) Math.ceil((double) totalMessages / pageSize);
            int lastPage = totalPages > 0 ? totalPages - 1 : 0;

            pageable = PageRequest.of(lastPage, pageSize, Sort.by(Sort.Direction.ASC, "timestamp"));
        } else {
            pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, "timestamp"));
        }

        Page<HistoryMessage> historyMessages =
                messageRepository.findHistoryMessageBySenderUsernameAndReceiverId(pageable, sender, receiver);

        return historyMessages.getContent().stream()
                .map(chatMapper::toMessageResponse)
                .toList();
    }



}
