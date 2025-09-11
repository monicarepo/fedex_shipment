package com.example.fedex.dto;

import com.example.fedex.entity.DeliveryMode;
import com.example.fedex.entity.ShippingDetails;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShippingDetailResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String fromName;
    private String contactNumber;
    private String shippingAddress;
    private DeliveryMode deliveryMode;
    private Double price;
    private String trackingNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ShippingDetailResponse(ShippingDetails shippingDetails) {
        this.id = shippingDetails.getId();
        this.firstName = shippingDetails.getFirstName();
        this.lastName = shippingDetails.getLastName();
        this.fromName = shippingDetails.getFromName();
        this.contactNumber = shippingDetails.getContactNumber();
        this.shippingAddress = shippingDetails.getShippingAddress();
        this.deliveryMode = shippingDetails.getDeliveryMode();
        this.price = shippingDetails.getPrice();
        this.trackingNumber = shippingDetails.getTrackingNumber();
        this.createdAt = shippingDetails.getCreatedAt();
        this.updatedAt = shippingDetails.getUpdatedAt();
    }
}
