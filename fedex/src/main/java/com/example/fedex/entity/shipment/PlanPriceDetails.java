package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plan_price_details")
public class PlanPriceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    private Integer pricingPlanId;

    private Integer detailId;

    private String fromWeight;

    private String toWeight;

    private Double price;

    public PlanPriceDetails() {}

    public PlanPriceDetails(Integer pricingPlanId, Integer detailId, String fromWeight, String toWeight, Double price) {
        this.pricingPlanId = pricingPlanId;
        this.detailId = detailId;
        this.fromWeight = fromWeight;
        this.toWeight = toWeight;
        this.price = price;
    }
}
