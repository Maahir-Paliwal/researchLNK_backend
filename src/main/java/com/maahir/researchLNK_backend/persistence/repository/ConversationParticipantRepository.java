package com.maahir.researchLNK_backend.persistence.repository;

import com.maahir.researchLNK_backend.persistence.model.ConversationParticipant;
import com.maahir.researchLNK_backend.persistence.model.keys.ConversationParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, ConversationParticipantId> {
    boolean existsByConversation_IdAndUser_Id(Long conversationId, Long userId);

    List<ConversationParticipant> findAllByConversation_Id(Long conversationId);
}
