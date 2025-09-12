package com.example.fedex.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "shipping-details")
public class ShippingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String senderFirstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String senderLastName;

    @NotBlank(message = "From name is required")
    @Size(max = 50)
    private String senderEmail;

    @NotBlank(message = "Contact Number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String senderContactNumber;

    @NotBlank(message = "Shipping Address is required")
    @Size(max = 150)
    private String senderAddress;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String receiverFirstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String receiverLastName;

    @NotBlank(message = "From name is required")
    @Size(max = 50)
    private String receiverEmail;

    @NotBlank(message = "Contact Number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String receiverContactNumber;

    @NotBlank(message = "Shipping Address is required")
    @Size(max = 150)
    private String receiverAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryMode deliveryMode;

    @DecimalMin(value = "0.0", message = "Price must be positive")
    private Double price;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    private String trackingNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ShippingDetails() {
        this.trackingNumber = generateTrackingNumber();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ShippingDetails(String senderFirstName, String senderLastName, String senderEmail, String senderContactNumber, String senderAddress, String receiverFirstName, String receiverLastName, String receiverEmail, String receiverContactNumber, String receiverAddress, DeliveryMode deliveryMode, Double price, ShippingStatus shippingStatus) {
        this();
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.senderEmail = senderEmail;
        this.senderContactNumber = senderContactNumber;
        this.senderAddress = senderAddress;
        this.receiverFirstName = receiverFirstName;
        this.receiverLastName = receiverLastName;
        this.receiverEmail = receiverEmail;
        this.receiverContactNumber = receiverContactNumber;
        this.receiverAddress = receiverAddress;
        this.deliveryMode = deliveryMode;
        this.price = price;
        this.shippingStatus = shippingStatus;
    }

    private String generateTrackingNumber() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 16)
                .toUpperCase();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
