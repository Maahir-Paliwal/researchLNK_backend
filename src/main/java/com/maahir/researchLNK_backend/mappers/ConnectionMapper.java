package com.maahir.researchLNK_backend.mappers;

import com.maahir.researchLNK_backend.dtos.connectiondtos.ConnectionDto;
import com.maahir.researchLNK_backend.persistence.model.Connection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConnectionMapper {


    ConnectionDto toDto(Connection connection);
}
