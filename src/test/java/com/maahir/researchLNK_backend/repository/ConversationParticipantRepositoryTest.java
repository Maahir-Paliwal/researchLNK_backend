package com.maahir.researchLNK_backend.repository;


import com.maahir.researchLNK_backend.persistence.model.Conversation;
import com.maahir.researchLNK_backend.persistence.model.ConversationParticipant;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.ConversationParticipantRepository;
import com.maahir.researchLNK_backend.persistence.repository.ConversationRepository;
import com.maahir.researchLNK_backend.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConversationParticipantRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private ConversationParticipantRepository conversationParticipantRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clearDatabase() {
        conversationParticipantRepository.deleteAll();
        conversationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testPersistConversationParticipant() {
        User user1 = userRepository.save(
                User.builder()
                        .userName("user1")
                        .email("user1@test.com")
                        .passwordHash("pw1")
                        .role(Role.RESEARCHER)
                        .isActive(true)
                        .name("User One")
                        .build()
        );

        Conversation conversation = conversationRepository.save(
                Conversation.builder()
                .title("title")
                .build()
        );

        ConversationParticipant conversationParticipant =
                ConversationParticipant.builder()
                        .lastReadAt(Timestamp)



    }
}
