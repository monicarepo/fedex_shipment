package com.example.fedex.repository;

import com.example.fedex.entity.shipment.PlanPriceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PricePlanDetailRepository extends JpaRepository<PlanPriceDetails, Integer> {
    Optional<PlanPriceDetails> findByPlanId(Integer planId);
    boolean existsByPricingPlanId(Integer planId);

    @Query(value = """
    SELECT * FROM plan_price_details p
    WHERE CAST(p.pricing_plan_id AS INTEGER) = :pricingPlanId
      AND CAST(:weight AS INTEGER) BETWEEN
          CAST(SUBSTRING(p.from_weight, 1, LENGTH(p.from_weight)-1) AS INTEGER)
      AND
          CAST(SUBSTRING(p.to_weight, 1, LENGTH(p.to_weight)-1) AS INTEGER)
    LIMIT 1
    """, nativeQuery = true)
    Optional<PlanPriceDetails> findPriceForWeight(@Param("pricingPlanId") Integer pricingPlanId,
                                                  @Param("weight") Integer weight);
}
