package com.example.fedex.entity.shipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String sex;

    public UserDetails() {}

    public UserDetails(String firstName, String middleName, String lastName, String sex) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.sex = sex;
    }
}

