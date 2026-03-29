package com.job.jobhunter.controller;

import com.job.jobhunter.domain.Message;
import com.job.jobhunter.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// MỚI THÊM 3 DÒNG IMPORT NÀY:
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
// LƯU Ý: @RequestMapping("/api/v1") chỉ áp dụng cho API HTTP (GET/POST/PUT/DELETE), KHÔNG áp dụng cho @MessageMapping
@RequestMapping("/api/v1")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate; // MỚI THÊM: Công cụ để đẩy tin nhắn realtime

    // MỚI THÊM: Cập nhật lại Constructor
    public MessageController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    // ... (Giữ nguyên các hàm @PostMapping, @GetMapping, @DeleteMapping cũ của bạn) ...

    // MỚI THÊM: HÀM XỬ LÝ CHAT WEBSOCKET
    @MessageMapping("/chat.send")
    public void sendMessageRealtime(@Payload Message message) {
        // 1. Lưu tin nhắn vào Database (Tùy chọn, giống như hàm POST của bạn)
        Message savedMessage = this.messageService.createMessage(message);

        // 2. Phát tin nhắn này ra cho tất cả những ai đang lắng nghe kênh "/topic/public"
        this.messagingTemplate.convertAndSend("/topic/public", savedMessage);
    }
}