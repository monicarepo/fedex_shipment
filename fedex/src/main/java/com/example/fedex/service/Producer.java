package com.example.fedex.service;

import com.example.fedex.entity.ShippingStatus;
import com.example.fedex.events.ShipmentStatusUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final String TOPIC = "shipment_details";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TwilioService twilioService;

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }

    @EventListener
    public void handleShipmentStatusUpdatedEvent(ShipmentStatusUpdatedEvent shipmentStatusUpdatedEvent) {
        Long shippingId = shipmentStatusUpdatedEvent.getUpdatedShipment().getShipmentId();
        ShippingStatus shippingStatus = shipmentStatusUpdatedEvent.getUpdatedShipment().getShippingStatus();
        String phoneNumber = shipmentStatusUpdatedEvent.getUpdatedShipment().getContactInfo().getPhoneNumber();
        String name = shipmentStatusUpdatedEvent.getUpdatedShipment().getUserDetails().getFirstName() + " " + shipmentStatusUpdatedEvent.getUpdatedShipment().getUserDetails().getLastName();
        String message = "Hi " + name + "Your shipping has been" + shippingStatus.name();
        twilioService.sendSms(phoneNumber,message);
        sendMessage("shipment has been changed, shippingId: " + shippingId + " with status " + shippingStatus);
    }
}
