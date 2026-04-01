package com.maahir.researchLNK_backend.mappers;

import com.maahir.researchLNK_backend.dtos.profiledtos.ProfileDto;
import com.maahir.researchLNK_backend.dtos.profiledtos.UpdateProfileRequestDto;
import com.maahir.researchLNK_backend.persistence.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mappings({
            @Mapping(target = "userName", source = "user.userName"),
            @Mapping(target = "userId", source = "user.id")
    })
    ProfileDto toDto(Profile profile);

    void updateProfile(UpdateProfileRequestDto request, @MappingTarget Profile profile);

}
