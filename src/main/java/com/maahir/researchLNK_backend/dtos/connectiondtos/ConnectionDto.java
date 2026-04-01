package com.maahir.researchLNK_backend.dtos.connectiondtos;

import com.maahir.researchLNK_backend.persistence.model.enums.ConnectionStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class ConnectionDto {
    private Long userLowId;
    private Long userHighId;
    private Long requesterId;
    private ConnectionStatus status;
    private Instant CreatedAt;
    private Instant UpdatedAt;
}
