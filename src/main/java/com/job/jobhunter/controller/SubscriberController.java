package com.job.jobhunter.controller;


import com.job.jobhunter.domain.Subscriber;
import com.job.jobhunter.service.SubscriberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("")
    public ResponseEntity<Subscriber> createSubscriber(@RequestBody Subscriber subscriberDetails) {

        this.subscriberService.createSubscriber(subscriberDetails);
        // Logic to create a subscriber
        return ResponseEntity.ok(subscriberDetails);
    }

}
