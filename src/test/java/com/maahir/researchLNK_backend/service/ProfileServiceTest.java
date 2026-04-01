package com.maahir.researchLNK_backend.service;

import com.maahir.researchLNK_backend.dtos.profiledtos.ProfileDto;
import com.maahir.researchLNK_backend.dtos.profiledtos.UpdateProfileRequestDto;
import com.maahir.researchLNK_backend.mappers.ProfileMapper;
import com.maahir.researchLNK_backend.persistence.model.Profile;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.repository.ProfileRepository;
import com.maahir.researchLNK_backend.services.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileService profileService;

    private User user;
    private Profile profile;
    private ProfileDto profileDto;
    private UpdateProfileRequestDto updateProfileRequestDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("maahir");

        profile = new Profile();
        profile.setId(10L);
        profile.setSummary("Original summary");
        profile.setInstitution("McGill");
        profile.setPosition("Student");
        profile.setProfilePictureKey("old-key");
        profile.setUser(user);

        profileDto = new ProfileDto();
        profileDto.setId(10L);
        profileDto.setUserName("maahir");
        profileDto.setSummary("Original summary");
        profileDto.setInstitution("McGill");
        profileDto.setPosition("Student");
        profileDto.setProfilePictureKey("old-key");

        updateProfileRequestDto = new UpdateProfileRequestDto();
        updateProfileRequestDto.setSummary("Updated summary");
        updateProfileRequestDto.setInstitution("UofT");
        updateProfileRequestDto.setPosition("Researcher");
        updateProfileRequestDto.setProfilePictureKey("new-key");
    }

    @Test
    void testGetProfileById_Success() {
        when(profileRepository.findById(10L)).thenReturn(Optional.of(profile));
        when(profileMapper.toDto(profile)).thenReturn(profileDto);

        ProfileDto result = profileService.getProfileById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("maahir", result.getUserName());
        assertEquals("Original summary", result.getSummary());

        verify(profileRepository).findById(10L);
        verify(profileMapper).toDto(profile);
    }

    @Test
    void testGetProfileById_ThrowsException_ProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.getProfileById(999L)
        );

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository).findById(999L);
        verify(profileMapper, never()).toDto(any(Profile.class));
    }

    @Test
    void testUpdateProfile_Success() {
        Profile updatedProfile = new Profile();
        updatedProfile.setId(10L);
        updatedProfile.setSummary("Updated summary");
        updatedProfile.setInstitution("UofT");
        updatedProfile.setPosition("Researcher");
        updatedProfile.setProfilePictureKey("new-key");
        updatedProfile.setUser(user);

        ProfileDto updatedDto = new ProfileDto();
        updatedDto.setId(10L);
        updatedDto.setUserName("maahir");
        updatedDto.setSummary("Updated summary");
        updatedDto.setInstitution("UofT");
        updatedDto.setPosition("Researcher");
        updatedDto.setProfilePictureKey("new-key");

        when(profileRepository.findById(10L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(updatedProfile);
        when(profileMapper.toDto(updatedProfile)).thenReturn(updatedDto);

        ProfileDto result = profileService.updateProfile(10L, 1L, updateProfileRequestDto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Updated summary", result.getSummary());
        assertEquals("UofT", result.getInstitution());
        assertEquals("Researcher", result.getPosition());
        assertEquals("new-key", result.getProfilePictureKey());

        verify(profileRepository).findById(10L);
        verify(profileMapper).updateProfile(updateProfileRequestDto, profile);
        verify(profileRepository).save(profile);
        verify(profileMapper).toDto(updatedProfile);
    }

    @Test
    void testUpdateProfile_ThrowsException_ProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.updateProfile(999L, 1L, updateProfileRequestDto)
        );

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository).findById(999L);
        verify(profileMapper, never()).updateProfile(any(UpdateProfileRequestDto.class), any(Profile.class));
        verify(profileRepository, never()).save(any(Profile.class));
        verify(profileMapper, never()).toDto(any(Profile.class));
    }

    @Test
    void testUpdateProfile_ThrowsException_UnauthorizedUser() {
        when(profileRepository.findById(10L)).thenReturn(Optional.of(profile));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.updateProfile(10L, 999L, updateProfileRequestDto)
        );

        assertEquals("You are not authorized to update this profile", exception.getMessage());
        verify(profileRepository).findById(10L);
        verify(profileMapper, never()).updateProfile(any(UpdateProfileRequestDto.class), any(Profile.class));
        verify(profileRepository, never()).save(any(Profile.class));
        verify(profileMapper, never()).toDto(any(Profile.class));
    }

    @Test
    void testSearchProfilesByUserName_ReturnsMatchingProfiles() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUserName("maahir2");

        Profile profile2 = new Profile();
        profile2.setId(11L);
        profile2.setSummary("Another summary");
        profile2.setInstitution("PMCC");
        profile2.setPosition("Intern");
        profile2.setProfilePictureKey("key-2");
        profile2.setUser(user2);

        ProfileDto profileDto2 = new ProfileDto();
        profileDto2.setId(11L);
        profileDto2.setUserName("maahir2");
        profileDto2.setSummary("Another summary");
        profileDto2.setInstitution("PMCC");
        profileDto2.setPosition("Intern");
        profileDto2.setProfilePictureKey("key-2");

        when(profileRepository.findByUser_UserNameContainingIgnoreCase("maah"))
                .thenReturn(List.of(profile, profile2));
        when(profileMapper.toDto(profile)).thenReturn(profileDto);
        when(profileMapper.toDto(profile2)).thenReturn(profileDto2);

        List<ProfileDto> result = profileService.searchProfilesByUserName("maah");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals(11L, result.get(1).getId());

        verify(profileRepository).findByUser_UserNameContainingIgnoreCase("maah");
        verify(profileMapper).toDto(profile);
        verify(profileMapper).toDto(profile2);
    }

    @Test
    void testSearchProfilesByUserName_ReturnsEmptyList_BlankQuery() {
        List<ProfileDto> result = profileService.searchProfilesByUserName("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(profileRepository, never()).findByUser_UserNameContainingIgnoreCase(anyString());
        verify(profileMapper, never()).toDto(any(Profile.class));
    }

    @Test
    void testSearchProfilesByUserName_ReturnsEmptyList_NoMatches() {
        when(profileRepository.findByUser_UserNameContainingIgnoreCase("nomatch"))
                .thenReturn(List.of());

        List<ProfileDto> result = profileService.searchProfilesByUserName("nomatch");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(profileRepository).findByUser_UserNameContainingIgnoreCase("nomatch");
        verify(profileMapper, never()).toDto(any(Profile.class));
    }
}