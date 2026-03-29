package com.job.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "conversations") // Đổi tên bảng cho đúng nghĩa hội thoại
@Getter
@Setter
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // GIỮ LẠI: Nội dung tin nhắn (Có thể dùng làm tin nhắn gần nhất hiển thị ở danh sách chat)
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    // GIỮ LẠI: Email người gửi (Để biết ai là người nhắn tin cuối cùng)
    private String senderEmail;

    // GIỮ LẠI: ID của phòng chat (Bạn có thể dùng làm mã tham chiếu hoặc mapping)
    private long conversationId;

    // GIỮ LẠI: Thời gian gửi/tạo
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant createdAt;

    // THÊM MỚI: Tên cuộc hội thoại để dễ quản lý trên giao diện
    private String name;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }
}