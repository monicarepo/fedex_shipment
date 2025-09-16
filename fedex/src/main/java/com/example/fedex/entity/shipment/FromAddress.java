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
}
