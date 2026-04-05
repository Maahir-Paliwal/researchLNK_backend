package com.maahir.researchLNK_backend.repository;

import com.maahir.researchLNK_backend.persistence.model.Conversation;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.ConversationRepository;
import com.maahir.researchLNK_backend.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepository;

    private User creator;

    @AfterEach
    public void clearDatabase() {
        conversationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        creator = User.builder()
                .userName("creator")
                .email("creator@test.com")
                .passwordHash("pw1")
                .role(Role.RESEARCHER)
                .isActive(true)
                .name("creator")
                .build();

        creator = userRepository.save(creator);
    }


    @Test
    public void testPersistConversation() {
        Conversation conversation = Conversation.builder()
                .creator(creator)
                .title("title")
                .build();
        conversation = conversationRepository.save(conversation);

        Long id = conversation.getId();

        Optional<Conversation> conversationOptional = conversationRepository.findById(id);

        assertThat(conversationOptional).isPresent();
        assertThat(conversationOptional.get().getId()).isEqualTo(conversation.getId());
        assertThat(conversationOptional.get().getTitle()).isEqualTo(conversation.getTitle());
        assertThat(conversationOptional.get().getCreator().getId()).isEqualTo(conversation.getCreator().getId());
        assertThat(conversationOptional.get().getCreatedAt()).isNotNull();
    }
}
