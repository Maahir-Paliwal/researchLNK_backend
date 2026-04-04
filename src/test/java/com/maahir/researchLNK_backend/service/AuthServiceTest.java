package com.maahir.researchLNK_backend.service;

import com.maahir.researchLNK_backend.dtos.auth.AuthResponse;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.UserRepository;
import com.maahir.researchLNK_backend.services.AuthService;
import com.maahir.researchLNK_backend.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .userName("testUser")
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .name("Test User")
                .role(Role.NON_RESEARCHER)
                .isActive(true)
                .build();
    }

    @Test
    public void register_WithValidData_ReturnsAuthResponse(){
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(userRepository.existsByUserName("new username")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(ArgumentMatchers.any(User.class))).thenReturn("jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        AuthResponse response = authService.register("new username","newuser@example.com", "password123", "New User");

        assertThat(response).isNotNull();
        assertThat(response.getJwt()).isEqualTo("jwt-token");
        assertThat(response.getExpiresIn()).isEqualTo(3600000L);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("newuser@example.com");
        assertThat(savedUser.getName()).isEqualTo("New User");
        assertThat(savedUser.getPasswordHash()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRole()).isEqualTo(Role.NON_RESEARCHER);
        assertThat(savedUser.isActive()).isTrue();
    }

    @Test
    public void register_WithExistingEmail_ThrowsException(){
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register("existing user", "existing@example.com", "password", "Existing User")
        );

        assertThat(exception).hasMessage("Email is already in use");
    }

    @Test
    public void register_GeneratesJwtToken() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(ArgumentMatchers.any(User.class))).thenReturn("generated-jwt");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        authService.register("user name", "user@example.com", "password", "User Name");

        verify(jwtService).generateToken(testUser);
    }

    @Test
    public void login_WithValidCredentials_ReturnsAuthResponse() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(testUser)).thenReturn("jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        AuthResponse response = authService.login("test@example.com", "password");

        assertThat(response).isNotNull();
        assertThat(response.getJwt()).isEqualTo("jwt-token");
        assertThat(response.getExpiresIn()).isEqualTo(3600000L);
        verify(jwtService).generateToken(testUser);
    }

    @Test
    public void login_WithNonExistentEmail_ThrowsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login("nonexistent@example.com", "password")
        );

        assertThat(exception).hasMessage("Invalid credentials");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    public void login_WithIncorrectPassword_ThrowsException(){
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login("test@example.com", "wrongpassword")
        );

        assertThat(exception).hasMessage("Invalid credentials");
        verify(passwordEncoder).matches("wrongpassword", "hashedPassword");
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    public void login_WithInactiveAccount_ThrowsException() {
        testUser.setActive(false);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> authService.login("test@example.com", "password")
        );

        assertThat(exception).hasMessage("Account is not active");
        verify(passwordEncoder).matches("password", "hashedPassword");
        verify(jwtService, never()).generateToken(any(User.class));

    }
}
