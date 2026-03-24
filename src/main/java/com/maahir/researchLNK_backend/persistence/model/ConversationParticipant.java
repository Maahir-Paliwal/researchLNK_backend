package com.maahir.researchLNK_backend.persistence.model;

import com.maahir.researchLNK_backend.persistence.model.keys.ConversationParticipantId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conversation_participants")
public class ConversationParticipant {

    @EmbeddedId
    @Builder.Default
    private ConversationParticipantId id = new ConversationParticipantId();

    @Column(name="joined_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp joinedAt;

    @Column(name="last_read_at")
    private Timestamp lastReadAt;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("conversationId")
    @JoinColumn(name="conversation_id", nullable = false)
    private Conversation conversation;
}
