package com.maahir.researchLNK_backend.repository;

import com.maahir.researchLNK_backend.persistence.model.PersonalPublication;
import com.maahir.researchLNK_backend.persistence.model.Profile;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.PersonalPublicationRepository;
import com.maahir.researchLNK_backend.persistence.repository.ProfileRepository;
import com.maahir.researchLNK_backend.persistence.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
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
public class PersonalPublicationRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private PersonalPublicationRepository personalPublicationRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clearDatabase() {
        personalPublicationRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testPersistPersonalPublication() {
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

        PersonalPublication personalPublication = PersonalPublication.builder()
                .profile(profile1)
                .build();

        personalPublication = personalPublicationRepository.save(personalPublication);

        Long id = personalPublication.getId();

        Optional<PersonalPublication> personalPublicationOptional = personalPublicationRepository.findById(id);

        assertThat(personalPublicationOptional).isPresent();
        assertThat(personalPublicationOptional.get().getId()).isEqualTo(personalPublication.getId());
        assertThat(personalPublicationOptional.get().getProfile()).isEqualTo(personalPublication.getProfile());
    }
}
