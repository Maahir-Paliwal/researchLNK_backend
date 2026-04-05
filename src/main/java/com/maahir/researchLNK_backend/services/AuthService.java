package com.maahir.researchLNK_backend.services;

import com.maahir.researchLNK_backend.dtos.auth.AuthResponse;
import com.maahir.researchLNK_backend.dtos.auth.UserResponse;
import com.maahir.researchLNK_backend.mappers.UserMapper;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/* TODO: Develop auth workflow around ORCID:
    1. User can sign up with email + password and LATER log in with ORCID
    2. User can sign up with ORCID
    3. A verified researcher can send a sign up link to a new user to sign up, giving researcher status automatically
    - IN CASE 1 OR 2, when ORCID is input, openAlex is queried for any publications
    - If publications found, then change their role to researcher and
        instantiate an empty profile, empty swipecard, and allow access to the instant messaging

 */


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthResponse register(String userName, String email, String password, String name) {
        if (userRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = User.builder()
                .userName(userName)
                .name(name)
                .passwordHash(passwordEncoder.encode(password))
                .email(email)
                .role(Role.NON_RESEARCHER)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .jwt(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }

    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email).
                orElseThrow(()-> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!user.isActive()) {
            throw new IllegalStateException("Account is not active");
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .jwt(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }

    public UserResponse getCurrentUser(User user){
        return userMapper.toUserResponse(user);
    }
}
