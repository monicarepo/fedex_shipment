package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "label_creation")
public class LabelCreation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;
    private java.sql.Date createdDate;
    private java.sql.Date updatedDate;

    public LabelCreation() {}

    public LabelCreation(Date createdDate, Date updatedDate) {
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
