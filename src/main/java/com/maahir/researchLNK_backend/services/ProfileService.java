package com.maahir.researchLNK_backend.services;

import com.maahir.researchLNK_backend.dtos.profiledtos.ProfileDto;
import com.maahir.researchLNK_backend.dtos.profiledtos.UpdateProfileRequestDto;
import com.maahir.researchLNK_backend.mappers.ProfileMapper;
import com.maahir.researchLNK_backend.persistence.model.Profile;
import com.maahir.researchLNK_backend.persistence.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public ProfileDto getProfileById(Long profileId) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return profileMapper.toDto(profile);
    }

    public ProfileDto updateProfile (
            Long profileId,
            Long authenticatedUserId,
            UpdateProfileRequestDto request) {

        Profile profile = profileRepository.findById(profileId).orElseThrow(
                () -> new EntityNotFoundException("Profile not found"));

        if (!profile.getUser().getId().equals(authenticatedUserId)) {
            throw new EntityNotFoundException("You are not authorized to update this profile");
        }

        profileMapper.updateProfile(request, profile);
        Profile updatedProfile = profileRepository.save(profile);

        return profileMapper.toDto(updatedProfile);
    }

    public List<ProfileDto> searchProfilesByUserName(String userNameQuery){
        if (userNameQuery.isBlank()) {
            return List.of();
        }

        return profileRepository.findByUser_UserNameContainingIgnoreCase(userNameQuery)
                .stream()
                .map(profileMapper::toDto)
                .toList();
    }

}
