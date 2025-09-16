package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "from_address")
public class FromAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private Integer userId;

    private String apt;

    private String name;

    private String city;

    private String state;

    private String zipcode;

    public  FromAddress() {}

    public FromAddress(Integer userId, String apt, String name, String city, String state, String zipcode) {
        this.userId = userId;
        this.apt = apt;
        this.name = name;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }
}
