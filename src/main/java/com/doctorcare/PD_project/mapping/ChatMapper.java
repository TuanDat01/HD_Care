package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.HistoryRequest;
import com.doctorcare.PD_project.dto.response.MessageResponse;
import com.doctorcare.PD_project.entity.HistoryMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface ChatMapper {
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    HistoryMessage toHistoryMessage(HistoryRequest historyRequest);

    MessageResponse toMessageResponse(HistoryMessage historyMessage);

}
