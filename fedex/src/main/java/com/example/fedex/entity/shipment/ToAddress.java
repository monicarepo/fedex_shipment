package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "to_address")
public class ToAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String apt;

    private String name;

    private String city;

    private String state;

    private String zipcode;

    private Integer userId;

    public ToAddress() {}

    public ToAddress(String apt, String name, String city, String state, String zipcode, Integer userId) {
        this.apt = apt;
        this.name = name;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.userId = userId;
    }
}


