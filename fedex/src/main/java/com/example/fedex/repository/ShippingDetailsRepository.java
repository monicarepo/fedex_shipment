package com.example.fedex.repository;

import com.example.fedex.entity.DeliveryMode;
import com.example.fedex.entity.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//DAO layer
@Repository
public interface ShippingDetailsRepository extends JpaRepository<ShippingDetails, Long> {
    Optional<ShippingDetails> findByTrackingNumber(String trackingNumber);
    List<ShippingDetails> findByFirstNameContainingIgnoreCase(String firstName);
    List<ShippingDetails> findByLastNameContainingIgnoreCase(String lastName);
    List<ShippingDetails> findByDeliveryMode(DeliveryMode deliveryMode);
    boolean existsByTrackingNumber(String trackingNumber);
}
