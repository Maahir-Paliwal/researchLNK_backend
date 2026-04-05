package com.maahir.researchLNK_backend.dtos.conversation;

import com.maahir.researchLNK_backend.dtos.conversationParticipant.ConversationParticipantDto;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class ConversationDto {
    private Long id;
    private String title;
    private Long creatorId;
    private Instant createdAt;
    private Set<ConversationParticipantDto> participants;

}
