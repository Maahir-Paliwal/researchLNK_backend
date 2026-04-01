package com.maahir.researchLNK_backend.dtos.profiledtos;

import lombok.Data;

@Data
public class ProfileDto {
    private Long id;
    private String userName;
    private String summary;
    private String institution;
    private String profilePictureKey;
    private String position;
}
