package com.maahir.researchLNK_backend.repository;


import com.maahir.researchLNK_backend.persistence.model.Profile;
import com.maahir.researchLNK_backend.persistence.model.SwipeCard;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.ProfileRepository;
import com.maahir.researchLNK_backend.persistence.repository.SwipeCardRepository;
import com.maahir.researchLNK_backend.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SwipeCardRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private SwipeCardRepository swipeCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @AfterEach
    void clearDatabase() {
        swipeCardRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testPersistSwipeCard() {
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
        Profile profile1 = profileRepository.save(
                Profile.builder()
                        .user(user1)
                        .build()
        );

        SwipeCard swipeCard = SwipeCard.builder()
                .profile(profile1)
                .build();

        swipeCard = swipeCardRepository.save(swipeCard);

        Long id = swipeCard.getId();

        Optional<SwipeCard> swipeCardOptional = swipeCardRepository.findById(id);

        assertThat(swipeCardOptional).isPresent();
        assertThat(swipeCardOptional.get().getId()).isEqualTo(swipeCard.getId());
        assertThat(swipeCardOptional.get().getProfile()).isEqualTo(swipeCard.getProfile());
    }
}
