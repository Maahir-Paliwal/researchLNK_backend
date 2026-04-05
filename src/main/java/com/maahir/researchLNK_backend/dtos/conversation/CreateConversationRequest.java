package com.maahir.researchLNK_backend.dtos.conversation;

import lombok.Data;

import java.util.Set;

@Data
public class CreateConversationRequest {
    private String title;
    private Set<Long> participantIds;
}
