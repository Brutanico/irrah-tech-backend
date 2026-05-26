package com.irrah.techbackend.repository;

import com.irrah.techbackend.entity.Conversation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, String> {

    List<Conversation> findByClient_IdOrderByLastMessageTimeDesc(String clientId);

    Optional<Conversation> findByIdAndClient_Id(String id, String clientId);
}
