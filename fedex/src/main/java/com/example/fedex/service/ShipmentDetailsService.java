package com.example.fedex.service;

import com.example.fedex.controller.ShipmentDetailsController;
import com.example.fedex.dto.ShippingDetailResponse;
import com.example.fedex.entity.DeliveryMode;
import com.example.fedex.entity.ShippingStatus;
import com.example.fedex.entity.shipment.*;
import com.example.fedex.events.ShipmentCreatedEvent;
import com.example.fedex.events.ShipmentStatusUpdatedEvent;
import com.example.fedex.repository.LabelCreationRepository;
import com.example.fedex.repository.PricePlanDetailRepository;
import com.example.fedex.repository.ShipmentDetailsRepository;
import com.example.fedex.repository.UserDetailRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public ShipmentDetailsService(ShipmentDetailsRepository shipmentDetailsRepository, PricePlanDetailRepository planPriceDetailsRepository, UserDetailRepository userDetailRepository) {
        this.shipmentDetailsRepository = shipmentDetailsRepository;
        this.planPriceDetailsRepository = planPriceDetailsRepository;
        this.userDetailRepository = userDetailRepository;
    }

    @Cacheable(value = "pricePlans", key = "'weight:' + #weightInGrams")
    public Optional<PlanPriceDetails> getPriceForWeight(int weightInGrams) {
        return planPriceDetailsRepository.findPriceForWeight(1, weightInGrams);
    }

    @Transactional
    @Caching(
        evict = {
            @CacheEvict(value = "shipmentDetailsAll", allEntries = true),
            @CacheEvict(value = "userShipments", allEntries = true)
        },
        put = {
            @CachePut(value = "shipmentDetails", key = "'shipment:' + #result.shipmentId", condition = "#result != null")
        }
    )
    public ShipmentDetails createShipment(ShipmentDetailsController.ShipmentInput input) {
        int weightInGrams = Integer.parseInt(input.weight().replace("g", ""));
        int qty = Integer.parseInt(input.qty());

//        PlanPriceDetails pricePlan = planPriceDetailsRepository
//                .findPriceForWeight(1, weightInGrams)
//                .orElseThrow(() -> new RuntimeException("No price found for given weight"));
        PlanPriceDetails pricePlan = getPriceForWeight(weightInGrams)
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
                input.shippingStatus(),
                totalPrice,
                tracking,
                labelCreation,
                contactInfo
        );

        ShipmentDetails savedShipmentDetails = shipmentDetailsRepository.save(shipment);

        //Label Generation
        generateLabelAsync(savedShipmentDetails);

        // Publish event instead of direct async call
//        applicationEventPublisher.publishEvent(new ShipmentCreatedEvent(savedShipmentDetails));
        return savedShipmentDetails;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleShipmentCreated(ShipmentCreatedEvent event) {
        generateLabelAsync(event.getShipment());
    }

    @Async
    @Caching(evict = {
        @CacheEvict(value = "shipmentDetails", key = "'shipment:' + #shipment.shipmentId")
    })
    public void generateLabelAsync(ShipmentDetails shipment) {
        try {
            LabelCreation generatedLabel = labelGenerationService.generateAndSaveLabel(shipment);
            shipment.setLabelCreation(generatedLabel);
            shipmentDetailsRepository.save(shipment);

//            LabelCreation existingLabel = shipment.getLabelCreation();
//            existingLabel.setPdfContent(generatedLabel.getPdfContent());
//            existingLabel.setFileName(generatedLabel.getFileName());
//            labelCreationRepository.save(existingLabel);

        } catch (Exception e) {
            logger.error("Async label generation failed for shipment {}", shipment.getShipmentId(), e);
        }
    }

    public LabelCreation generateAndSaveLabel(ShipmentDetails shipment) {
        return labelGenerationService.generateAndSaveLabel(shipment);
    }

    @Cacheable(value = "shipmentDetailsAll", key = "'allShipments'")
    public List<ShipmentDetails> getAllShipments() {
        return shipmentDetailsRepository.findAll();
    }

    @Cacheable(value = "userShipments", key = "'user:' + #userId")
    public List<ShipmentDetails> getShipmentsByUserId(Long userId) {
        return shipmentDetailsRepository.findByUserDetails_UserId(userId);
    }

    @Cacheable(value = "shipmentDetails", key = "'shipment:' + #shipmentId")
    public Optional<ShipmentDetails> getShipmentDetails(Integer shipmentId) {
        return shipmentDetailsRepository.findByShipmentId(shipmentId);
    }

    @Cacheable(value = "shipmentDetails", key = "'tracking:' + #trackingNumber")
    public Optional<ShipmentDetails> getShipmentDetailsByTrackingNumber(String trackingNumber) {
        return shipmentDetailsRepository.findByTrackingNumber(trackingNumber);
    }

    @Caching(evict = {
            @CacheEvict(value = "shipmentDetails", key = "'shipment:' + #shipmentId"),
            @CacheEvict(value = "shipmentDetailsAll", allEntries = true),
            @CacheEvict(value = "userShipments", allEntries = true)
    })
    public boolean deleteShipmentDetails(Integer shipmentId) {
        if (shipmentDetailsRepository.existsById(shipmentId)) {
            shipmentDetailsRepository.deleteById(shipmentId);
            return true;
        }
        return false;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "shipmentDetails", key = "'shipment:' + #shipmentId")
    })
    public LabelCreation generateLabelForShipment(Integer shipmentId) {
        ShipmentDetails shipment = getShipmentDetails(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));

        LabelCreation generatedLabel = labelGenerationService.generateAndSaveLabel(shipment);
        shipment.setLabelCreation(generatedLabel);
        shipmentDetailsRepository.save(shipment);
        return generatedLabel;
    }

    @Transactional()
    @Cacheable(value = "labels", key = "'label:' + #labelId")
    public LabelCreation getLabelWithContent(Long labelId) {
        return labelCreationRepository.findByIdWithContent(labelId)
                .orElseThrow(() -> new RuntimeException("Label not found"));
    }

    @Caching(
        evict = {
            @CacheEvict(value = "shipmentDetailsAll", allEntries = true),
            @CacheEvict(value = "userShipments", allEntries = true)
        },
        put = {
            @CachePut(value = "shipmentDetails", key = "'shipment:' + #shipmentId", condition = "#result != null")
        }
    )
    @Transactional
    public ShipmentDetails updateShipmentDetail(Integer shipmentId, ShipmentDetailsController.ShipmentInput input) {
        ShipmentDetails existingShipment = shipmentDetailsRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));

        int weightInGrams = Integer.parseInt(input.weight().replace("g", ""));
        int qty = Integer.parseInt(input.qty());

//        PlanPriceDetails pricePlan = planPriceDetailsRepository
//                .findPriceForWeight(1, weightInGrams)
//                .orElseThrow(() -> new RuntimeException("No price found for given weight"));

        PlanPriceDetails pricePlan = getPriceForWeight(weightInGrams)
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

        existingShipment.setWeight(input.weight());
        existingShipment.setQty(input.qty());
        existingShipment.setModeOfDelivery(input.modeOfDelivery());
        existingShipment.setShippingStatus(input.shippingStatus());
        existingShipment.setPrice(totalPrice);

        generateLabelAsync(existingShipment);

        return shipmentDetailsRepository.save(existingShipment);
    }

    @Transactional
    @Caching(
        evict = {
            @CacheEvict(value = "shipmentDetailsAll", allEntries = true),
            @CacheEvict(value = "userShipments", allEntries = true)
        },
        put = {
            @CachePut(value = "shipmentDetails", key = "'shipment:' + #shipmentId", condition = "#result != null")
        }
    )
    public ShipmentDetails updateShipmentStatus(Integer shipmentId, ShipmentDetailsController.ShipmentStatusInput input) {
        ShipmentDetails existingShipment = shipmentDetailsRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));
        existingShipment.setShippingStatus(input.shippingStatus());
        ShipmentDetails updatedShipment = shipmentDetailsRepository.save(existingShipment);
        applicationEventPublisher.publishEvent(new ShipmentStatusUpdatedEvent(updatedShipment));
        return updatedShipment;
    }


//    Testing with static data
    public void testGenerateLabelAsyncWithStaticData() {
        try {
            logger.info("Starting label generation test with static data...");
            ShipmentDetails shipmentDetails = createStaticShipmentForTesting();
            ShipmentDetails savedShipmentDetails = shipmentDetailsRepository.save(shipmentDetails);
            logger.info("Test shipment created with ID: {}", savedShipmentDetails.getShipmentId());
            generateLabelAsync(savedShipmentDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ShipmentDetails createStaticShipmentForTesting() {
        UserDetails userDetails = new UserDetails("Monica","Amalanathan","A","Female");
        UserDetails savedUser = userDetailRepository.save(userDetails);

        FromAddress fromAddress = new FromAddress(
                Math.toIntExact(savedUser.getUserId()),
                "Apt 101",
                "John Doe",
                "New York",
                "NY",
                "10001"
        );

        ToAddress toAddress = new ToAddress(
                "Apt 202",
                "Jane Smith",
                "Los Angeles",
                "CA",
                "90001",
                Math.toIntExact(savedUser.getUserId())
        );

        ContactInfo contactInfo = new ContactInfo(
                Math.toIntExact(savedUser.getUserId()),
                "123-456-7890",
                "john.doe@email.com",
                new java.sql.Date(System.currentTimeMillis()),
                new java.sql.Date(System.currentTimeMillis())
        );

        Tracking tracking = new Tracking();
        tracking.setUserId(Math.toIntExact(savedUser.getUserId()));

        LabelCreation labelCreation = new LabelCreation();

        return new ShipmentDetails(
                userDetails,
                "500g",
                "2",
                fromAddress,
                toAddress,
                DeliveryMode.EXPRESS,
                ShippingStatus.PENDING,
                25.99,
                tracking,
                labelCreation,
                contactInfo
        );

    }

}
