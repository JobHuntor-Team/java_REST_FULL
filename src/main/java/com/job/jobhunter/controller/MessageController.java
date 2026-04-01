package com.job.jobhunter.controller;

import com.job.jobhunter.domain.Message;
import com.job.jobhunter.service.MessageService;
import com.job.jobhunter.util.annotation.ApiMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    // 1. LẤY TOÀN BỘ TIN NHẮN CỦA CONVERSATION (SCRUM-9)
    // URL Postman: GET {{BASE_URL}}/api/v1/conversations/2/messages
    @GetMapping("/conversations/{id}/messages")
    @ApiMessage("Fetch all messages for a specific conversation")
    public ResponseEntity<Page<Message>> getMessagesByConversation(
            @PathVariable("id") long conversationId,
            Pageable pageable) {

        // Trả về toàn bộ trang dữ liệu từ Service
        Page<Message> allMessages = this.messageService.getMessagesByConversation(conversationId, pageable);
        return ResponseEntity.ok(allMessages);
    }

    // 2. TẠO TIN NHẮN (POST /api/v1/messages)
    @PostMapping("/messages")
    @ApiMessage("Create a new message")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message msg) {
        Message savedMessage = this.messageService.createMessage(msg);
        this.messagingTemplate.convertAndSend("/topic/public", savedMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    // 3. XÓA TIN NHẮN (DELETE /api/v1/messages/1)
    @DeleteMapping("/messages/{id}")
    @ApiMessage("Delete a message")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") long id) {
        this.messageService.deleteMessage(id);
        return ResponseEntity.ok(null);
    }

    // 4. WEBSOCKET CHAT (SCRUM-8)
    @MessageMapping("/chat.send")
    public void sendMessageRealtime(@Payload Message message) {
        Message savedMessage = this.messageService.createMessage(message);
        this.messagingTemplate.convertAndSend("/topic/public", savedMessage);
    }
}
//test