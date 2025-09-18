package com.example.fedex.events;

import com.example.fedex.entity.shipment.ShipmentDetails;
import lombok.Data;

@Data
public class ShipmentStatusUpdatedEvent {
    private final ShipmentDetails updatedShipment;

    public ShipmentStatusUpdatedEvent(ShipmentDetails updatedShipment) {
        this.updatedShipment = updatedShipment;
    }
}
