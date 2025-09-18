package com.example.fedex.dto;

import com.example.fedex.controller.ShipmentDetailsController;
import com.example.fedex.entity.DeliveryMode;
import com.example.fedex.entity.ShippingStatus;
import com.example.fedex.entity.shipment.*;
import lombok.Data;

@Data
public class ShipmentDetailResponse {
    private Long shipmentId;
    private UserDetails userDetails;
    private String weight;
    private String qty;
    private FromAddress fromAddress;
    private ToAddress toAddress;
    private DeliveryMode modeOfDelivery;
    private ShippingStatus shippingStatus;
    private Double price;
    private Tracking tracking;
    private LabelCreation labelCreation;
    private ContactInfo contactInfo;

    public ShipmentDetailResponse() {}

    public ShipmentDetailResponse(ShipmentDetails shipmentDetails) {
        this.shipmentId = shipmentDetails.getShipmentId();
        this.userDetails = shipmentDetails.getUserDetails();
        this.weight = shipmentDetails.getWeight();
        this.qty = shipmentDetails.getQty();
        this.fromAddress = shipmentDetails.getFromAddress();
        this.toAddress = shipmentDetails.getToAddress();
        this.modeOfDelivery = shipmentDetails.getModeOfDelivery();
        this.shippingStatus = shipmentDetails.getShippingStatus();
        this.price = shipmentDetails.getPrice();
        this.tracking = shipmentDetails.getTracking();
        this.labelCreation = shipmentDetails.getLabelCreation();
        this.contactInfo = shipmentDetails.getContactInfo();
    }
}
