package com.example.fedex.repository;

import com.example.fedex.entity.shipment.PlanPriceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PricePlanDetailRepository extends JpaRepository<PlanPriceDetails, Integer> {
    Optional<PlanPriceDetails> findByPlanId(Integer planId);
    boolean existsByPricingPlanId(Integer planId);
}
