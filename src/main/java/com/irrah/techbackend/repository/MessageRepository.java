package com.irrah.techbackend.repository;

import com.irrah.techbackend.domain.MessagePriority;
import com.irrah.techbackend.domain.MessageStatus;
import com.irrah.techbackend.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findByConversation_IdOrderByTimestampAsc(String conversationId);

    List<Message> findBySender_IdOrderByTimestampDesc(String senderId);

    List<Message> findBySender_IdAndStatusOrderByTimestampDesc(String senderId, MessageStatus status);

    List<Message> findBySender_IdAndPriorityOrderByTimestampDesc(String senderId, MessagePriority priority);

    List<Message> findBySender_IdAndStatusAndPriorityOrderByTimestampDesc(
            String senderId,
            MessageStatus status,
            MessagePriority priority
    );
}
