package com.example.fedex.service;

import com.example.fedex.controller.ShipmentDetailsController;
import com.example.fedex.entity.shipment.*;
import com.example.fedex.repository.LabelCreationRepository;
import com.example.fedex.repository.PricePlanDetailRepository;
import com.example.fedex.repository.ShipmentDetailsRepository;
import com.example.fedex.repository.UserDetailRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional()
public class ShipmentDetailsService {
    private final ShipmentDetailsRepository shipmentDetailsRepository;
    private final PricePlanDetailRepository planPriceDetailsRepository;
    private final UserDetailRepository userDetailRepository;

    private static final Logger logger = LoggerFactory.getLogger(ShipmentDetailsService.class);

    @Autowired
    private LabelGenerationService labelGenerationService;

    @Autowired
    private LabelCreationRepository labelCreationRepository;

    public ShipmentDetailsService(ShipmentDetailsRepository shipmentDetailsRepository, PricePlanDetailRepository planPriceDetailsRepository, UserDetailRepository userDetailRepository) {
        this.shipmentDetailsRepository = shipmentDetailsRepository;
        this.planPriceDetailsRepository = planPriceDetailsRepository;
        this.userDetailRepository = userDetailRepository;
    }

    @Transactional
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

        ShipmentDetails savedShipmentDetails = shipmentDetailsRepository.save(shipment);

        //Label Generation
        generateLabelAsync(savedShipmentDetails);
        return savedShipmentDetails;
    }

    @Async
    public void generateLabelAsync(ShipmentDetails shipment) {
        try {
            LabelCreation generatedLabel = labelGenerationService.generateAndSaveLabel(shipment);
            shipment.setLabelCreation(generatedLabel);
            shipmentDetailsRepository.save(shipment);
        } catch (Exception e) {
            logger.error("Async label generation failed for shipment {}", shipment.getShipmentId(), e);
        }
    }

    public LabelCreation generateAndSaveLabel(ShipmentDetails shipment) {
        return labelGenerationService.generateAndSaveLabel(shipment);
    }

    public List<ShipmentDetails> getAllShipments() {
        return shipmentDetailsRepository.findAll();
    }

    public Optional<ShipmentDetails> getShipmentDetails(Integer shipmentId) {
        return shipmentDetailsRepository.findByShipmentId(shipmentId);
    }

    public boolean deleteShipmentDetails(Integer shipmentId) {
        if (shipmentDetailsRepository.existsById(shipmentId)) {
            shipmentDetailsRepository.deleteById(shipmentId);
            return true;
        }
        return false;
    }

    @Transactional
    public LabelCreation generateLabelForShipment(Integer shipmentId) {
        ShipmentDetails shipment = getShipmentDetails(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));

        LabelCreation generatedLabel = labelGenerationService.generateAndSaveLabel(shipment);
        shipment.setLabelCreation(generatedLabel);
        shipmentDetailsRepository.save(shipment);
        return generatedLabel;
    }

    @Transactional()
    public LabelCreation getLabelWithContent(Long labelId) {
        return labelCreationRepository.findByIdWithContent(labelId)
                .orElseThrow(() -> new RuntimeException("Label not found"));
    }

    @Transactional
    public ShipmentDetails updateShipmentDetail(Integer shipmentId, ShipmentDetailsController.ShipmentInput input) {
        ShipmentDetails existingShipment = shipmentDetailsRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));

        int weightInGrams = Integer.parseInt(input.weight().replace("g", ""));
        int qty = Integer.parseInt(input.qty());

        PlanPriceDetails pricePlan = planPriceDetailsRepository
                .findPriceForWeight(1, weightInGrams)
                .orElseThrow(() -> new RuntimeException("No price found for given weight"));

        double totalPrice = pricePlan.getPrice() * qty;

        UserDetails userDetails = existingShipment.getUserDetails();
        userDetails.setFirstName(input.userDetails().firstName());
        userDetails.setMiddleName(input.userDetails().middleName());
        userDetails.setLastName(input.userDetails().lastName());
        userDetails.setSex(input.userDetails().sex());

        FromAddress fromAddress = existingShipment.getFromAddress();
        fromAddress.setApt(input.fromAddress().apt());
        fromAddress.setName(input.fromAddress().name());
        fromAddress.setCity(input.fromAddress().city());
        fromAddress.setState(input.fromAddress().state());
        fromAddress.setZipcode(input.fromAddress().zipcode());

        ToAddress toAddress = existingShipment.getToAddress();
        toAddress.setApt(input.toAddress().apt());
        toAddress.setName(input.toAddress().name());
        toAddress.setCity(input.toAddress().city());
        toAddress.setState(input.toAddress().state());
        toAddress.setZipcode(input.toAddress().zipcode());

        ContactInfo contactInfo = existingShipment.getContactInfo();
        contactInfo.setPhoneNumber(input.contactInfo().phoneNumber());
        contactInfo.setEmail(input.contactInfo().email());
        contactInfo.setUpdatedDate(new java.sql.Date(System.currentTimeMillis()));

        Tracking tracking = existingShipment.getTracking();
        tracking.setUpdatedDate(new java.sql.Date(System.currentTimeMillis()));

//        LabelCreation labelCreation = existingShipment.getLabelCreation();
//        labelCreation.setUpdatedDate(new java.sql.Date(System.currentTimeMillis()));

        existingShipment.setWeight(input.weight());
        existingShipment.setQty(input.qty());
        existingShipment.setModeOfDelivery(input.modeOfDelivery());
        existingShipment.setPrice(totalPrice);

        generateLabelAsync(existingShipment);

        return shipmentDetailsRepository.save(existingShipment);
    }

}
