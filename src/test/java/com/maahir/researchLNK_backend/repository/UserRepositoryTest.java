package com.maahir.researchLNK_backend.repository;


import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
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
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clearDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void testPersistUser() {
        User user = User.builder()
                .userName("userName")
                .email("testemail@123.com")
                .passwordHash("passwordHash")
                .role(Role.RESEARCHER)
                .isActive(true)
                .name("testName")
                .build();

        user = userRepository.save(user);

        Long id = user.getId();

        Optional<User> userOptional = userRepository.findById(id);

        assertThat(userOptional).isPresent();
        assertThat(userOptional.get().getUserName()).isEqualTo(user.getUserName());
        assertThat(userOptional.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(userOptional.get().getPasswordHash()).isEqualTo(user.getPasswordHash());
        assertThat(userOptional.get().getRole()).isEqualTo(user.getRole());
        assertThat(userOptional.get().getCreatedAt()).isNotNull();
        assertThat(userOptional.get().isActive()).isEqualTo(user.isActive());
        assertThat(userOptional.get().getName()).isEqualTo(user.getName());
    }
}
