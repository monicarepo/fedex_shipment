package com.example.fedex.dto;

import com.example.fedex.entity.DeliveryMode;
import com.example.fedex.entity.ShippingDetails;
import com.example.fedex.entity.ShippingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShippingDetailResponse {
    private Long id;
    private String senderFirstName;
    private String senderLastName;
    private String senderEmail;
    private String senderContactNumber;
    private String shippingAddress;
    private String senderAddress;
    private String receiverFirstName;
    private String receiverLastName;
    private String receiverEmail;
    private String receiverContactNumber;
    private String receiverAddress;
    private DeliveryMode deliveryMode;
    private ShippingStatus shippingStatus;
    private Double price;
    private String trackingNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ShippingDetailResponse() {}

    public ShippingDetailResponse(ShippingDetails shippingDetails) {
        this.id = shippingDetails.getId();
        this.senderFirstName = shippingDetails.getSenderFirstName();
        this.senderLastName = shippingDetails.getSenderLastName();
        this.senderEmail = shippingDetails.getSenderEmail();
        this.senderContactNumber = shippingDetails.getSenderContactNumber();
        this.senderAddress = shippingDetails.getSenderAddress();
        this.receiverFirstName = shippingDetails.getReceiverFirstName();
        this.receiverLastName = shippingDetails.getReceiverLastName();
        this.receiverEmail = shippingDetails.getReceiverEmail();
        this.receiverContactNumber = shippingDetails.getReceiverContactNumber();
        this.receiverAddress = shippingDetails.getReceiverAddress();
        this.deliveryMode = shippingDetails.getDeliveryMode();
        this.shippingStatus = shippingDetails.getShippingStatus();
        this.price = shippingDetails.getPrice();
        this.trackingNumber = shippingDetails.getTrackingNumber();
        this.createdAt = shippingDetails.getCreatedAt();
        this.updatedAt = shippingDetails.getUpdatedAt();
    }
}
