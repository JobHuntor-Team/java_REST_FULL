package com.job.jobhunter.controller;

import com.job.jobhunter.domain.Subscriber;
import com.job.jobhunter.service.SubscriberService;
import com.job.jobhunter.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    // 1. API TẠO MỚI (POST)
    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> create(@RequestBody Subscriber subscriber) throws IdInvalidException {
        Subscriber newSubscriber = this.subscriberService.createSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSubscriber);
    }

    // 2. API CẬP NHẬT (PUT)
    @PutMapping("/subscribers")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subscriber) throws IdInvalidException {
        Subscriber updatedSubscriber = this.subscriberService.updateSubscriber(subscriber);
        return ResponseEntity.ok(updatedSubscriber);
    }

    // 3. API XÓA (DELETE)
    @DeleteMapping("/subscribers/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        this.subscriberService.deleteSubscriber(id);
        return ResponseEntity.ok().body(null);
    }

    // 4. API LẤY DANH SÁCH (GET)
    @GetMapping("/subscribers")
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        List<Subscriber> listSubscribers = this.subscriberService.getAllSubscribers();
        return ResponseEntity.ok(listSubscribers);
    }

    // 5. API LẤY THÔNG TIN 1 NGƯỜI DÙNG (GET by ID)
    @GetMapping("/subscribers/{id}")
    public ResponseEntity<Subscriber> getSubscriberById(@PathVariable("id") long id) {
        Subscriber sub = this.subscriberService.getSubscriberById(id);
        return ResponseEntity.ok(sub);
    }

}