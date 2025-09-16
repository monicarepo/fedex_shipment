package com.example.fedex.repository;

import com.example.fedex.entity.shipment.ShipmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentDetailsRepository extends JpaRepository<ShipmentDetails, Integer>  {
    Optional<ShipmentDetails> findByShipmentId(Integer shipmentId);
}
