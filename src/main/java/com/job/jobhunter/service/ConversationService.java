package com.job.jobhunter.service;

import com.job.jobhunter.domain.Conversation;
import com.job.jobhunter.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    // 1. LẤY DANH SÁCH HỘI THOẠI
    public List<Conversation> getAllConversations() {
        return this.conversationRepository.findAll();
    }

    // 2. TẠO CUỘC HỘI THOẠI MỚI
    public Conversation createConversation(Conversation conversation) {
        return this.conversationRepository.save(conversation);
    }

    // 3. XÓA MỘT HỘI THOẠI
    public void deleteConversation(long id) {
        this.conversationRepository.deleteById(id);
    }
    public List<Conversation> getUserConversations(long userId) {
        // Đúng ra sẽ là: return this.conversationRepository.findByUserId(userId);
        return this.conversationRepository.findAll();
    }
}