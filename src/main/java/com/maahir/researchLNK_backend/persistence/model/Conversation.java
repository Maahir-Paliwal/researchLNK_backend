package com.maahir.researchLNK_backend.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name="created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "conversation")
    private Set<ConversationParticipant> participants;

    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages;
}
