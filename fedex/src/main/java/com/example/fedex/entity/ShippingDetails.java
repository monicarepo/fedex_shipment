package com.example.fedex.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "From name is required")
    @Size(max = 50)
    private String fromName;

    @NotBlank(message = "Contact Number is required")
    @Size(max = 10)
    private String contactNumber;

    @NotBlank(message = "Shipping Address is required")
    @Size(max = 150)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryMode deliveryMode;

    @DecimalMin(value = "0.0", message = "Price must be positive")
    private Double price;

    private String trackingNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ShippingDetails() {
        this.trackingNumber = generateTrackingNumber();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ShippingDetails(String firstName, String lastName, String fromName, String contactNumber,
                           String shippingAddress, DeliveryMode deliveryMode, Double price) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.fromName = fromName;
        this.contactNumber = contactNumber;
        this.shippingAddress = shippingAddress;
        this.deliveryMode = deliveryMode;
        this.price = price;
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
