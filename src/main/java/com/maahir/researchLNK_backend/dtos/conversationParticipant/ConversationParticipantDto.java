package com.maahir.researchLNK_backend.dtos.conversationParticipant;

import lombok.Data;

import java.time.Instant;

@Data
public class ConversationParticipantDto {
    private Long userId;
    private Long conversationId;
    private String userName;
    private Instant joinedAt;
    private Instant lastReadAt;
}
