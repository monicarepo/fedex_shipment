package com.example.fedex.controller;

import com.example.fedex.dto.LabelResponse;
import com.example.fedex.dto.ShipmentDetailResponse;
import com.example.fedex.entity.DeliveryMode;
import com.example.fedex.entity.shipment.*;
import com.example.fedex.service.Producer;
import com.example.fedex.service.ShipmentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ShipmentDetailsController {
    @Autowired
    private ShipmentDetailsService shipmentDetailsService;

    @Autowired
    private Producer producer;

    @MutationMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ShipmentDetailResponse createShipmentDetail(@Argument ShipmentInput input) {
        ShipmentDetails shipment = shipmentDetailsService.createShipment(input);
        return new ShipmentDetailResponse(shipment);
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ShipmentDetailResponse> getAllShipments() {
        return shipmentDetailsService.getAllShipments().stream()
                .map(ShipmentDetailResponse::new)
                .collect(Collectors.toList());
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<ShipmentDetailResponse> getShipmentById(@Argument Integer id){
        return shipmentDetailsService.getShipmentDetails(id).map(ShipmentDetailResponse::new);
    }

    @MutationMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ShipmentDetailResponse updateShipmentDetail(@Argument Integer id, @Argument ShipmentInput input) {
        ShipmentDetails updatedShipment = shipmentDetailsService.updateShipmentDetail(id, input);
        return new ShipmentDetailResponse(updatedShipment);
    }

    @MutationMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public boolean deleteShipmentDetail(@Argument Integer id) {
        return shipmentDetailsService.deleteShipmentDetails(id);
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public LabelResponse generateShippingLabel(@Argument Integer shipmentId) {
        try {
            LabelCreation labelCreation = shipmentDetailsService.generateLabelForShipment(shipmentId);
            return new LabelResponse(labelCreation);
        } catch (Exception e) {
            throw new RuntimeException("Error generating label: " + e.getMessage());
        }
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public LabelResponse getShippingLabel(@Argument Integer shipmentId) {
        try {
            ShipmentDetails shipment = shipmentDetailsService.getShipmentDetails(shipmentId)
                    .orElseThrow(() -> new RuntimeException("Shipment not found"));

            if (shipment.getLabelCreation() == null) {
                throw new RuntimeException("No label found for this shipment");
            }
            LabelCreation labelCreation = shipmentDetailsService.getLabelWithContent(
                    shipment.getLabelCreation().getLabelId()
            );
            return new LabelResponse(labelCreation);

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving label: " + e.getMessage());
        }
    }

    @MutationMapping
    public String createTestMessage(@Argument String message) {
        producer.sendMessage(message);
        return "Message sent";
    }


    public record ShipmentInput(
            UserInput userDetails,
            String weight,
            String qty,
            FromAddressInput fromAddress,
            ToAddressInput toAddress,
            DeliveryMode modeOfDelivery,
            Double price,
            Tracking tracking,
            LabelCreation labelCreation,
            ContactInfoInput contactInfo
    ) {}

    public record UserInput(
            Integer userId,
            String firstName,
            String middleName,
            String lastName,
            String sex
    ){}

    public record FromAddressInput(
            Integer userId,
            String apt,
            String name,
            String city,
            String state,
            String zipcode
    ) {}

    public record ToAddressInput(
            Integer userId,
            String apt,
            String name,
            String city,
            String state,
            String zipcode
    ) {}

    public record ContactInfoInput(
            Integer userId,
            String phoneNumber,
            String email
    ) {}

}
