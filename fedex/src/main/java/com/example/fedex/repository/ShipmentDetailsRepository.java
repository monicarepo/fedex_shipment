package com.example.fedex.repository;

import com.example.fedex.entity.shipment.ShipmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentDetailsRepository extends JpaRepository<ShipmentDetails, Integer>  {
    Optional<ShipmentDetails> findByShipmentId(Integer shipmentId);

    @Query("SELECT s FROM ShipmentDetails s JOIN s.tracking t WHERE t.trackingNumber = :trackingNumber")
    Optional<ShipmentDetails> findByTrackingNumber(@Param("trackingNumber") String trackingNumber);

    @Query("SELECT s FROM ShipmentDetails s WHERE s.userDetails.userId = :userId")
    List<ShipmentDetails> findByUserDetails_UserId(@Param("userId") Long userId);

    @Query("SELECT s FROM ShipmentDetails s WHERE s.shippingStatus = :status")
    List<ShipmentDetails> findByShippingStatus(@Param("status") String status);

    @Query("SELECT COUNT(s) FROM ShipmentDetails s WHERE s.userDetails.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
}
