package com.example.fedex.dto;

import com.example.fedex.entity.shipment.PlanPriceDetails;
import lombok.Data;

@Data
public class PlanPriceDetailResponse {
    private Integer planId;
    private Integer pricingPlanId;
    private Integer detailId;
    private String fromWeight;
    private String toWeight;
    private Double price;

    public PlanPriceDetailResponse() {}

    public PlanPriceDetailResponse(PlanPriceDetails planPriceDetails) {
        this.planId = planPriceDetails.getPlanId();
        this.pricingPlanId = planPriceDetails.getPricingPlanId();
        this.detailId = planPriceDetails.getDetailId();
        this.fromWeight = planPriceDetails.getFromWeight();
        this.toWeight = planPriceDetails.getToWeight();
        this.price = planPriceDetails.getPrice();
    }
}
