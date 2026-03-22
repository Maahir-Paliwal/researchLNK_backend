package com.maahir.researchLNK_backend.persistence.model;

import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="user_name", unique = true, nullable = false)
    private String userName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "name")
    private String name;

    @Column(name = "orcid_id")
    private String orcidId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "user_vector", columnDefinition = "vector(384)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    private float[] userVector;

    @Column(name = "impression_vector", columnDefinition = "vector(384)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    private float[] impressionVector;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @OneToMany(mappedBy = "user")
    private Set<ConversationParticipant> conversationParticipants;

    @OneToMany(mappedBy = "user")
    private Set<Message> messages;

    @ManyToMany
    @JoinTable(name="users_liked_articles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="article_id")
            )
    private Set<Article> likedArticles;
}