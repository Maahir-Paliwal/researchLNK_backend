package com.maahir.researchLNK_backend.persistence.repository;

import com.maahir.researchLNK_backend.persistence.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findDistinctByParticipants_User_IdOrderByCreatedAtDesc(Long userId);
}
