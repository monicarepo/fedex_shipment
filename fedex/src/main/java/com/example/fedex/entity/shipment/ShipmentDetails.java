package com.example.fedex.entity;

import com.example.fedex.entity.shipment.*;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shipment_details")
public class ShipmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private UserDetails userDetails;

    private String weight;
    private String qty;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_address_id", referencedColumnName = "addressId")
    private FromAddress fromAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_address_id", referencedColumnName = "addressId")
    private ToAddress toAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryMode modeOfDelivery;
    private Double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tracking_id", referencedColumnName = "labelId")
    private Tracking tracking;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "label_id", referencedColumnName = "labelId")
    private LabelCreation labelCreation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id", referencedColumnName = "contactId")
    private ContactInfo contactInfo;
}

