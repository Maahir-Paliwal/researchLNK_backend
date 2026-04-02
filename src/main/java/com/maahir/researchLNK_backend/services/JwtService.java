package com.maahir.researchLNK_backend.services;

import com.maahir.researchLNK_backend.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }
}
