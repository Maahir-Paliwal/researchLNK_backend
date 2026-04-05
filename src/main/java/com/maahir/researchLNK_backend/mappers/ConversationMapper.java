package com.maahir.researchLNK_backend.mappers;

import com.maahir.researchLNK_backend.dtos.conversation.ConversationDto;
import com.maahir.researchLNK_backend.persistence.model.Conversation;
import com.maahir.researchLNK_backend.persistence.model.ConversationParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    @Mapping(target = "participants", source = "participants")
    ConversationDto toDto(Conversation conversation, List<ConversationParticipant> participants);
}
