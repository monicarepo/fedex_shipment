package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

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

    public Tracking() {}

    public Tracking(Integer userId, String trackingNumber, Date createdDate, Date updatedDate) {
        this.userId = userId;
        this.trackingNumber = trackingNumber;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
