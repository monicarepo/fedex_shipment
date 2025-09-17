package com.example.fedex.service;

import com.example.fedex.entity.shipment.ShipmentDetails;
import lombok.Getter;

@Getter
public class ShipmentCreatedEvent {
    private final ShipmentDetails shipment;

    public ShipmentCreatedEvent(ShipmentDetails shipment) {
        this.shipment = shipment;
    }

}
