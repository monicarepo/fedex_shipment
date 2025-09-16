package com.example.fedex.entity.shipment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Date;

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

    public  Member() {}

    public Member(String fullName, String firstName, String lastName, String email, String phoneNumber, String membershipType, Date registrationDate, String status) {
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.membershipType = membershipType;
        this.registrationDate = registrationDate;
        this.status = status;
    }
}
