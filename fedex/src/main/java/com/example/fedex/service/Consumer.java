package com.example.fedex.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    @KafkaListener(topics = "shipment_details", groupId = "fedex-shipment-group")
    public void receiveMessage(String message) {
        System.out.println("Received Message: " + message);
    }
}
