package com.example.fedex.controller;


import com.example.fedex.dto.PlanPriceDetailResponse;
import com.example.fedex.service.PricePlanDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class PricePlanDetailController {
    @Autowired
    private PricePlanDetailService pricePlanDetailService;

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<PlanPriceDetailResponse> allPricePlanDetails() {
        return pricePlanDetailService.getAllPricePlanDetails();
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<PlanPriceDetailResponse> getPricePlanDetailById(@Argument Integer id) {
        return pricePlanDetailService.getPricePlanDetailById(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PlanPriceDetailResponse createPlanPriceDetails(@Argument @Valid PricePlanInput input) {
        PlanPriceDetailResponse dto = new PlanPriceDetailResponse();
        dto.setPricingPlanId(input.pricingPlanId);
        dto.setDetailId(input.detailId);
        dto.setFromWeight(input.fromWeight);
        dto.setToWeight(input.toWeight);
        dto.setPrice(input.price);
        return pricePlanDetailService.createPricePlan(dto);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<PlanPriceDetailResponse> updatePricePlanDetails(@Argument Integer id, @Argument @Valid PricePlanInput input) {
        PlanPriceDetailResponse dto = new PlanPriceDetailResponse();
        dto.setPricingPlanId(input.pricingPlanId);
        dto.setDetailId(input.detailId);
        dto.setFromWeight(input.fromWeight);
        dto.setToWeight(input.toWeight);
        dto.setPrice(input.price);
        return pricePlanDetailService.updatePricePlan(id,dto);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deletePricePlanDetails(@Argument Integer id) {
        return pricePlanDetailService.deletePricePlan(id);
    }

    public record PricePlanInput(
        Integer pricingPlanId,
        Integer detailId,
        String fromWeight,
        String toWeight,
        Double price
    ){}
}
