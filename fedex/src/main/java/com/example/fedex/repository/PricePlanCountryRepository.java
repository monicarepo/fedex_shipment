package com.example.fedex.repository;

import com.example.fedex.entity.shipment.PlanCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PricePlanCountryRepository extends JpaRepository<PlanCountry, Integer> {
    Optional<PlanCountry> findByPlanId(Integer planId);
}
