package com.example.fedex.entity.shipment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "members")
public class Member {
    @Id
    private Integer memberId;

    private String fullName;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String membershipType;

    private java.sql.Date registrationDate;

    private String status = "Active";
}
