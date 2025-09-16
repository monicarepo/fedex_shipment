package com.example.fedex.entity.shipment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "plan_country")
public class PlanCountry {
    @Id
    private Integer planId;

    private Integer pricingPlanId;

    private String countryCode;

    public PlanCountry() {}

    public PlanCountry(Integer planId, Integer pricingPlanId, String countryCode) {
        this.planId = planId;
        this.pricingPlanId = pricingPlanId;
        this.countryCode = countryCode;
    }
}
