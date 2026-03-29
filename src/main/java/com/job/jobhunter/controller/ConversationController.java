package com.job.jobhunter.controller;

import com.job.jobhunter.domain.Conversation;
import com.job.jobhunter.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    // 1. LẤY DANH SÁCH HỘI THOẠI (GET)
    @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> getUserConversations() {
        List<Conversation> listConversations = this.conversationService.getAllConversations();
        return ResponseEntity.ok(listConversations);
    }

    // 2. TẠO CUỘC HỘI THOẠI MỚI (POST)
    @PostMapping("/conversations")
    public ResponseEntity<Conversation> createConversation(@RequestBody Conversation conversation) {
        Conversation newConv = this.conversationService.createConversation(conversation);
        return ResponseEntity.status(201).body(newConv);
    }

    // 3. XÓA HỘI THOẠI (DELETE)
    @DeleteMapping("/conversations/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable("id") long id) {
        this.conversationService.deleteConversation(id);
        return ResponseEntity.ok().body(null);
    }
    @GetMapping("/conversations/user/{userId}")
    public ResponseEntity<List<Conversation>> getUserConversations(@PathVariable("userId") long userId) {
        List<Conversation> listConversations = this.conversationService.getUserConversations(userId);
        return ResponseEntity.ok(listConversations);
    }
}