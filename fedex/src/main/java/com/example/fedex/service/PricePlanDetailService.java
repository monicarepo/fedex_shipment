package com.example.fedex.service;

import com.example.fedex.dto.PlanPriceDetailResponse;
import com.example.fedex.entity.shipment.PlanPriceDetails;
import com.example.fedex.repository.PricePlanDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PricePlanDetailService {
    @Autowired
    private PricePlanDetailRepository pricePlanDetailRepository;

    public List<PlanPriceDetailResponse> getAllPricePlanDetails() {
        return pricePlanDetailRepository.findAll().stream()
                .map(PlanPriceDetailResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<PlanPriceDetailResponse> getPricePlanDetailById(Integer id) {
        return pricePlanDetailRepository.findByPlanId(id).map(PlanPriceDetailResponse::new);
    }

    public PlanPriceDetailResponse createPricePlan(PlanPriceDetailResponse planPriceDetailResponse) {
        PlanPriceDetails planPriceDetails = new PlanPriceDetails();
        mapToEntity(planPriceDetailResponse, planPriceDetails);
        PlanPriceDetails savedPlanPriceDetail = pricePlanDetailRepository.save(planPriceDetails);
        return new PlanPriceDetailResponse(savedPlanPriceDetail);
    }

    private void mapToEntity(PlanPriceDetailResponse dto, PlanPriceDetails entity) {
        entity.setPricingPlanId(dto.getPricingPlanId());
        entity.setDetailId(dto.getDetailId());
        entity.setFromWeight(dto.getFromWeight());
        entity.setToWeight(dto.getToWeight());
        entity.setPrice(dto.getPrice());
    }

    public Optional<PlanPriceDetailResponse> updatePricePlan(Integer id, PlanPriceDetailResponse planPriceDetailResponse) {
        return pricePlanDetailRepository.findById(id).map( existPlan -> {
            mapToEntity(planPriceDetailResponse, existPlan);
            PlanPriceDetails updatedPlanDetail = pricePlanDetailRepository.save(existPlan);
            return new PlanPriceDetailResponse(updatedPlanDetail);
        });
    }

    public boolean deletePricePlan(Integer id) {
        if (pricePlanDetailRepository.existsById(id)) {
            pricePlanDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
