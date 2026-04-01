package com.maahir.researchLNK_backend.services;

import com.maahir.researchLNK_backend.dtos.connectiondtos.ConnectionDto;
import com.maahir.researchLNK_backend.mappers.ConnectionMapper;
import com.maahir.researchLNK_backend.persistence.model.Connection;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.ConnectionStatus;
import com.maahir.researchLNK_backend.persistence.model.keys.ConnectionId;
import com.maahir.researchLNK_backend.persistence.repository.ConnectionRepository;
import com.maahir.researchLNK_backend.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;
    private final ConnectionMapper connectionMapper;


    public ConnectionDto requestConnection(Long authenticatedUserId, Long otherUserId) {
        if (authenticatedUserId.equals(otherUserId)) {
            throw new IllegalArgumentException("You cannot request a connection with yourself");
        }
        User requester = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User otherUser = userRepository.findById(otherUserId).
                orElseThrow(() -> new IllegalArgumentException("User not found"));

        ConnectionId connectionId = normalizeConnectionId(authenticatedUserId, otherUserId);

        Connection existingConnection = connectionRepository.findById(connectionId).orElse(null);

        // will NEVER be null following the logic operations below
        Connection savedConnection = null;
        if (existingConnection != null) {
            if (existingConnection.getStatus() == ConnectionStatus.PENDING) {
                if (existingConnection.getRequester().getId().equals(authenticatedUserId)) {
                    throw new IllegalStateException("Connection request already sent");
                }
                // PENDING -> ACCEPTED
                else {
                    existingConnection.setStatus(ConnectionStatus.ACCEPTED);
                    savedConnection = connectionRepository.saveAndFlush(existingConnection);
                }
                if (existingConnection.getStatus() == ConnectionStatus.ACCEPTED) {
                    throw new IllegalStateException("Connection already accepted");
                }
            }
        } else {
            User userLow = requester.getId() < otherUserId ? requester : otherUser;
            User userHigh = requester.getId() < otherUserId ? otherUser : requester;

            Connection connection = Connection.builder()
                    .status(ConnectionStatus.PENDING)
                    .requester(requester)
                    .userLow(userLow)
                    .userHigh(userHigh)
                    .build();

            savedConnection = connectionRepository.saveAndFlush(connection);
        }

        return connectionMapper.toDto(savedConnection);
    }

    public ConnectionDto acceptConnection(Long authenticatedUserId, Long otherUserId) {
        User authenticatedUser = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User otherUser = userRepository.findById(otherUserId).
                orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (authenticatedUser.getId().equals(otherUserId)) {
            throw new IllegalArgumentException("You cannot accept a connection with yourself");
        }

        ConnectionId connectionId = normalizeConnectionId(authenticatedUserId, otherUserId);
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("Connection not found"));

        if (connection.getRequester().getId().equals(authenticatedUserId)) {
            throw new IllegalStateException("Requester cannot accept their own connection request");
        }
        if (connection.getStatus() != ConnectionStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be accepted");
        }

        connection.setStatus(ConnectionStatus.ACCEPTED);
        connection = connectionRepository.saveAndFlush(connection);
        return connectionMapper.toDto(connection);
    }

    public void rejectOrDeleteConnection(Long authenticatedUserId, Long otherUserId) {
        User authenticatedUser = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User otherUser = userRepository.findById(otherUserId).
                orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (authenticatedUser.getId().equals(otherUserId)) {
            throw new IllegalArgumentException("You cannot reject or delete a connection with yourself");
        }

        ConnectionId connectionId = normalizeConnectionId(authenticatedUserId, otherUserId);

        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot delete a connection that does not exist"));

        if (connection.getRequester().getId().equals(authenticatedUserId)) {
            throw new IllegalStateException("Requester cannot reject their own connection request");
        }

        connectionRepository.deleteById(connectionId);
    }


    private ConnectionId normalizeConnectionId(Long user1Id, Long user2Id) {
        return new ConnectionId(Math.min(user1Id, user2Id), Math.max(user1Id, user2Id));
    }
}
