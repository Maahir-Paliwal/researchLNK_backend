package com.maahir.researchLNK_backend.dtos.auth;

import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String userName;
    private String email;
    private Role role;
    private boolean isActive;
    private String name;
    private String orcidId;
}
