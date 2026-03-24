package com.maahir.researchLNK_backend.repository;

import com.maahir.researchLNK_backend.persistence.model.Profile;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.ProfileRepository;
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
public class ProfileRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clearDatabase() {
        profileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testPersistProfile(){
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

        Profile profile1 = Profile.builder()
                        .user(user1)
                        .build();

        Profile profile = profileRepository.save(profile1);

        Long id = profile.getId();

        Optional<Profile> profileOptional = profileRepository.findById(id);

        assertThat(profileOptional).isPresent();
        assertThat(profileOptional.get().getId()).isEqualTo(profile.getId());
        assertThat(profileOptional.get().getUser()).isEqualTo(profile.getUser());
    }
}
