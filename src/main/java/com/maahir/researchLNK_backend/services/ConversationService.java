package com.maahir.researchLNK_backend.services;

import com.maahir.researchLNK_backend.dtos.conversation.ConversationDto;
import com.maahir.researchLNK_backend.dtos.conversation.CreateConversationRequest;
import com.maahir.researchLNK_backend.mappers.ConversationMapper;
import com.maahir.researchLNK_backend.persistence.model.Conversation;
import com.maahir.researchLNK_backend.persistence.model.ConversationParticipant;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.persistence.model.enums.ConnectionStatus;
import com.maahir.researchLNK_backend.persistence.model.enums.Role;
import com.maahir.researchLNK_backend.persistence.repository.ConnectionRepository;
import com.maahir.researchLNK_backend.persistence.repository.ConversationParticipantRepository;
import com.maahir.researchLNK_backend.persistence.repository.ConversationRepository;
import com.maahir.researchLNK_backend.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final ConversationMapper conversationMapper;

    public ConversationDto createConversation(Long authenticatedUserId, CreateConversationRequest request) {
        User creator = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Creator not found"));

        if (creator.getRole() != Role.RESEARCHER) {
            throw new IllegalStateException("Only researchers can create conversations");
        }

        Set<Long> participantIds = request.getParticipantIds() == null
                ? new HashSet<>()
                : new HashSet<>(request.getParticipantIds());

        participantIds.remove(authenticatedUserId);

        if (participantIds.isEmpty()) {
            throw new IllegalArgumentException("Conversation must have at least one participant");
        }

        List<User> invitedUsers = userRepository.findAllById(participantIds);

        if (invitedUsers.size() != participantIds.size()) {
            throw new IllegalArgumentException("One or more participants not found");
        }

        for (User user : invitedUsers) {
            validateAcceptedConnectionBetween(authenticatedUserId, user.getId());
        }

        Conversation conversation = Conversation.builder()
                .title(request.getTitle())
                .creator(creator)
                .build();

        conversation = conversationRepository.save(conversation);

        List<ConversationParticipant> conversationParticipants = new ArrayList<>();

        conversationParticipants.add(
                ConversationParticipant.builder()
                        .conversation(conversation)
                        .user(creator)
                        .lastReadAt(null)
                        .build()
        );

        for (User invitedUser : invitedUsers) {
            conversationParticipants.add(
                    ConversationParticipant.builder()
                            .conversation(conversation)
                            .user(invitedUser)
                            .lastReadAt(null)
                            .build()
            );
        }

        conversationParticipants = conversationParticipantRepository.saveAll(conversationParticipants);

        conversation.setParticipants(new HashSet<>(conversationParticipants));

        return conversationMapper.toDto(conversation, conversationParticipants);
    }

    public ConversationDto getConversation(Long authenticatedUserId, Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        User user = userRepository.findById(authenticatedUserId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.RESEARCHER) {
            throw new IllegalStateException("Only researchers can view conversations");
        }

        assertUserIsParticipant(conversationId, authenticatedUserId);

        List<ConversationParticipant> participants = conversationParticipantRepository
                .findAllByConversation_Id(conversationId);


        return conversationMapper.toDto(conversation, participants);
    }

    public List<ConversationDto> getMyConversations(Long authenticatedUserId) {
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.RESEARCHER) {
            throw new IllegalStateException("Only researchers can view conversations");
        }

        List<Conversation> conversations =
                conversationRepository.findDistinctByParticipants_User_IdOrderByCreatedAtDesc(authenticatedUserId);

        return conversations.stream()
                .map(conversation -> {
                    List<ConversationParticipant> participants = conversationParticipantRepository
                            .findAllByConversation_Id(conversation.getId());
                    return conversationMapper.toDto(conversation, participants);
                })
                .collect(Collectors.toList());
    }


    private void validateAcceptedConnectionBetween(Long userAId, Long UserBId) {
        Long userLowId = Math.min(userAId, UserBId);
        Long userHighId = Math.max(userAId, UserBId);

        boolean isConnected = connectionRepository
                .existsByIdUserLowIdAndIdUserHighIdAndStatus(
                        userLowId,
                        userHighId,
                        ConnectionStatus.ACCEPTED
                );

        if (!isConnected) {
            throw new IllegalStateException("You can only create conversations with users you are connected to");
        }
    }

    private void assertUserIsParticipant(Long conversationId, Long userId) {
         boolean isParticipant = conversationParticipantRepository
                .existsByConversation_IdAndUser_Id(conversationId, userId);

         if (!isParticipant) {
             throw new IllegalArgumentException("User is not a participant of this conversation");
         }
    }
}
