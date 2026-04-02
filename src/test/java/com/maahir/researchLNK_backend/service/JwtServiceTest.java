package com.maahir.researchLNK_backend.service;


import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.properties.JwtProperties;
import com.maahir.researchLNK_backend.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class JwtServiceTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtService jwtService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .userName("testUser")
                .name("Test User")
                .role(Role.NON_RESEARCHER)
                .isActive(true)
                .build();

        String testSecretKey = "ZdMcB2od4uvyZx0ksy2nisgko0NWI9JNdH5Hd4xOL0I=";
        when(jwtProperties.getSecretKey()).thenReturn(testSecretKey);
    }

    @Test
    public void testGenerateToken_Success() {
        when(jwtProperties.getExpirationTime()).thenReturn(1000L);

        String token = jwtService.generateToken(testUser);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    public void extractUsername_ReturnsCorrectEmail(){
        when(jwtProperties.getExpirationTime()).thenReturn(1000L);

        String token = jwtService.generateToken(testUser);

        String extractedUsername = jwtService.extractUsername(token);

        assertThat(extractedUsername).isEqualTo(testUser.getEmail());
    }

    @Test
    public void isTokenValid_WithValidToken_ReturnsTrue(){
        when(jwtProperties.getExpirationTime()).thenReturn(1000L);

        String token = jwtService.generateToken(testUser);

        boolean isValid = jwtService.isTokenValid(token, testUser);

        assertThat(isValid).isTrue();
    }

    @Test
    public void isTokenValid_WithDifferentUser_ReturnsFalse(){
        when(jwtProperties.getExpirationTime()).thenReturn(1000L);

        String token = jwtService.generateToken(testUser);

        User differentUser = new User();
        differentUser.setEmail("different@example.com");

        boolean isValid = jwtService.isTokenValid(token, differentUser);

        assertThat(isValid).isFalse();
    }

    @Test
    public void getExpirationTime_ReturnsCorrectValue(){
        when(jwtProperties.getExpirationTime()).thenReturn(1000L);

        long expirationTime = jwtService.getExpirationTime();

        assertThat(expirationTime).isEqualTo(1000L);
    }
}
