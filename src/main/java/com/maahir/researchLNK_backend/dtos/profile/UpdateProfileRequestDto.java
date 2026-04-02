package com.maahir.researchLNK_backend.dtos.profile;


import lombok.Data;

@Data
public class UpdateProfileRequestDto {
    private String summary;
    private String institution;
    private String profilePictureKey;
    private String position;
}
