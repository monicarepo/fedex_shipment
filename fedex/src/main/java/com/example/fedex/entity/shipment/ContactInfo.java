package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

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

    public  ContactInfo() {
        this.createdDate = new java.sql.Date(System.currentTimeMillis());
        this.updatedDate = new java.sql.Date(System.currentTimeMillis());
    }

    public ContactInfo(Integer userId, String phoneNumber, String email, Date createdDate, Date updatedDate) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
