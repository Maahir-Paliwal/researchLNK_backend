package com.maahir.researchLNK_backend.repository;

import com.maahir.researchLNK_backend.persistence.model.Conversation;
import com.maahir.researchLNK_backend.persistence.model.ConversationParticipant;
import com.maahir.researchLNK_backend.persistence.model.Message;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.ConversationRepository;
import com.maahir.researchLNK_backend.persistence.repository.MessageRepository;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MessageRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clearDatabase() {
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testPersistMessage() {
        User user = userRepository.save(
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
                        .creator(user)
                        .title("title")
                        .build()
        );

        Message message = Message.builder()
                .content("content")
                .conversation(conversation)
                .messageSender(user)
                .build();

        message = messageRepository.save(message);

        Long id = message.getId();

        Optional<Message> messageOptional = messageRepository.findById(id);

        assertThat(messageOptional).isPresent();
        assertThat(messageOptional.get().getId()).isEqualTo(message.getId());
        assertThat(messageOptional.get().getContent()).isEqualTo(message.getContent());
        assertThat(messageOptional.get().getConversation()).isEqualTo(message.getConversation());
        assertThat(messageOptional.get().getMessageSender()).isEqualTo(message.getMessageSender());
    }

}
