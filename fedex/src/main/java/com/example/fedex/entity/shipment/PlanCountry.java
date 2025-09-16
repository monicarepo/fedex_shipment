package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plan_country")
public class PlanCountry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    private Integer pricingPlanId;

    private String countryCode;

    public PlanCountry() {}

    public PlanCountry(Integer pricingPlanId, String countryCode) {
        this.pricingPlanId = pricingPlanId;
        this.countryCode = countryCode;
    }
}
