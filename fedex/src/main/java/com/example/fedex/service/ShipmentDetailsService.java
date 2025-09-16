package com.example.fedex.service;

import com.example.fedex.controller.ShipmentDetailsController;
import com.example.fedex.entity.shipment.*;
import com.example.fedex.repository.PricePlanDetailRepository;
import com.example.fedex.repository.ShipmentDetailsRepository;
import com.example.fedex.repository.UserDetailRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentDetailsService {
    private final ShipmentDetailsRepository shipmentDetailsRepository;
    private final PricePlanDetailRepository planPriceDetailsRepository;
    private final UserDetailRepository userDetailRepository;

    public ShipmentDetailsService(ShipmentDetailsRepository shipmentDetailsRepository, PricePlanDetailRepository planPriceDetailsRepository, UserDetailRepository userDetailRepository) {
        this.shipmentDetailsRepository = shipmentDetailsRepository;
        this.planPriceDetailsRepository = planPriceDetailsRepository;
        this.userDetailRepository = userDetailRepository;
    }

    public ShipmentDetails createShipment(ShipmentDetailsController.ShipmentInput input) {
        int weightInGrams = Integer.parseInt(input.weight().replace("g", ""));
        int qty = Integer.parseInt(input.qty());

        PlanPriceDetails pricePlan = planPriceDetailsRepository
                .findPriceForWeight(1, weightInGrams)
                .orElseThrow(() -> new RuntimeException("No price found for given weight"));

        double totalPrice = pricePlan.getPrice() * qty;

        UserDetails userDetails = new UserDetails(
                input.userDetails().firstName(),
                input.userDetails().middleName(),
                input.userDetails().lastName(),
                input.userDetails().sex()
        );

        UserDetails savedUserDetails = userDetailRepository.save(userDetails);

        FromAddress fromAddress = new FromAddress(
                Math.toIntExact(savedUserDetails.getUserId()),
                input.fromAddress().apt(),
                input.fromAddress().name(),
                input.fromAddress().city(),
                input.fromAddress().state(),
                input.fromAddress().zipcode()
        );

        ToAddress toAddress = new ToAddress(
                input.toAddress().apt(),
                input.toAddress().name(),
                input.toAddress().city(),
                input.toAddress().state(),
                input.toAddress().zipcode(),
                Math.toIntExact(savedUserDetails.getUserId())
        );

        ContactInfo contactInfo = new ContactInfo(
                Math.toIntExact(savedUserDetails.getUserId()),
                input.contactInfo().phoneNumber(),
                input.contactInfo().email(),
                new java.sql.Date(System.currentTimeMillis()),
                new java.sql.Date(System.currentTimeMillis())
        );

        Tracking tracking = new Tracking();
        tracking.setUserId(Math.toIntExact(savedUserDetails.getUserId()));

        LabelCreation labelCreation = new LabelCreation();

        ShipmentDetails shipment = new ShipmentDetails(
                userDetails,
                input.weight(),
                input.qty(),
                fromAddress,
                toAddress,
                input.modeOfDelivery(),
                totalPrice,
                tracking,
                labelCreation,
                contactInfo
        );

        return shipmentDetailsRepository.save(shipment);
    }

    public List<ShipmentDetails> getAllShipments() {
        return shipmentDetailsRepository.findAll();
    }

    public Optional<ShipmentDetails> getShipmentDetails(Integer shipmentId) {
        return shipmentDetailsRepository.findByShipmentId(shipmentId);
    }

}
