package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "contact_info")
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    private Integer userId;

    private String phoneNumber;

    private String email;

    private java.sql.Date createdDate;

    private java.sql.Date updatedDate;
}
