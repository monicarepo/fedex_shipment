//package com.example.fedex.entity;
//
//import com.example.fedex.entity.shipment.*;
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "shipment_details")
//public class ShipmentDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long shipmentId;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
//    private UserDetails userDetails;
//    private String weight;
//    private String qty;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "from_address_id", referencedColumnName = "address_id")
//    private FromAddress fromAddress;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "to_address_id", referencedColumnName = "address_id")
//    private ToAddress toAddress;
//
//    @Enumerated(EnumType.STRING)
//    private DeliveryMode modeOfDelivery;
//    private Double price;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "tracking_id", referencedColumnName = "label_id")
//    private Tracking tracking;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "label_id", referencedColumnName = "label_id")
//    private LabelCreation labelCreation;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
//    private ContactInfo contactInfo;
//}
//
