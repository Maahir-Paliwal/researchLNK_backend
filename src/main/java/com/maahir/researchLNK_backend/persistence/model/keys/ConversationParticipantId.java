package com.maahir.researchLNK_backend.persistence.model.keys;


import com.maahir.researchLNK_backend.persistence.model.Conversation;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ConversationParticipantId {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "conversation_id")
    private Long conversationId;
}
