package com.maahir.researchLNK_backend.repository;

import com.maahir.researchLNK_backend.persistence.model.Conversation;
import com.maahir.researchLNK_backend.persistence.repository.ConversationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConversationRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private ConversationRepository conversationRepository;

    @AfterEach
    public void clearDatabase() {
        conversationRepository.deleteAll();
    }

    @Test
    public void testPersistConversation() {
        Conversation conversation = Conversation.builder()
                .title("title")
                .build();
        conversation = conversationRepository.save(conversation);

        Long id = conversation.getId();

        Optional<Conversation> conversationOptional = conversationRepository.findById(id);

        assertThat(conversationOptional).isPresent();
        assertThat(conversationOptional.get().getId()).isEqualTo(conversation.getId());
        assertThat(conversationOptional.get().getTitle()).isEqualTo(conversation.getTitle());
        assertThat(conversationOptional.get().getCreatedAt()).isNotNull();
    }
}
