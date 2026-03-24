package com.maahir.researchLNK_backend.repository;

import com.maahir.researchLNK_backend.persistence.model.Connection;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.ConnectionStatus;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.model.keys.ConnectionId;
import com.maahir.researchLNK_backend.persistence.repository.ConnectionRepository;
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
public class ConnectionRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @AfterEach
    public void clearDatabase() {
        connectionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testPersistConnection() {
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

        User user2 = userRepository.save(
                User.builder()
                        .userName("user2")
                        .email("user2@test.com")
                        .passwordHash("pw2")
                        .role(Role.RESEARCHER)
                        .isActive(true)
                        .name("User Two")
                        .build()
        );

        Connection connection = Connection.builder()
                .id(new ConnectionId())
                .status(ConnectionStatus.PENDING)
                .requester(user1)
                .userLow(user1)
                .userHigh(user2)
                .build();

        connection = connectionRepository.saveAndFlush(connection);

        ConnectionId connectionId = connection.getId();

        Optional<Connection> connectionOptional = connectionRepository.findById(connectionId);

        assertThat(connectionOptional).isPresent();
        assertThat(connectionOptional.get().getId()).isEqualTo(connectionId);
        assertThat(connectionOptional.get().getStatus()).isEqualTo(ConnectionStatus.PENDING);
        assertThat(connectionOptional.get().getCreatedAt()).isNotNull();
        assertThat(connectionOptional.get().getUpdatedAt()).isNotNull();
        assertThat(connectionOptional.get().getRequester()).isEqualTo(connection.getRequester());
        assertThat(connectionOptional.get().getUserLow()).isEqualTo(connection.getUserLow());
        assertThat(connectionOptional.get().getUserHigh()).isEqualTo(connection.getUserHigh());
    }
}
