package com.job.jobhunter.service;

import com.job.jobhunter.domain.Message;
import com.job.jobhunter.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        return this.messageRepository.save(message);
    }

    // TỐI ƯU 3: Cập nhật hàm để trả về Page
    public Page<Message> getMessagesByConversation(long conversationId, Pageable pageable) {
        return this.messageRepository.findByConversationId(conversationId, pageable);
    }

    public void deleteMessage(long id) {
        this.messageRepository.deleteById(id);
    }
}