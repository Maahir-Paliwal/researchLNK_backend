package com.maahir.researchLNK_backend.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String jwt;
    private long expiresIn;
}
