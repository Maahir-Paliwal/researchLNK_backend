package com.maahir.researchLNK_backend.repository;


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

    }
}
