package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tracking")
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;

    private Integer userId;

    private String trackingNumber;

    private java.sql.Date createdDate;

    private java.sql.Date updatedDate;
}
