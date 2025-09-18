package com.example.fedex.security.shipment;

import com.example.fedex.entity.shipment.PlanCountry;
import com.example.fedex.entity.shipment.PlanPriceDetails;
import com.example.fedex.repository.PricePlanCountryRepository;
import com.example.fedex.repository.PricePlanDetailRepository;
import com.example.fedex.service.ShipmentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final PricePlanCountryRepository planCountryRepository;

    @Autowired
    private ShipmentDetailsService shipmentDetailsService;

    @Autowired
    private PricePlanDetailRepository pricePlanDetailRepository;

    public DataSeeder(PricePlanCountryRepository planCountryRepository) {
        this.planCountryRepository = planCountryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedPlanCountry();
        seedPricePlanDetails();
    }

    private void seedPlanCountry() {
        if (planCountryRepository.count() == 0) {
            planCountryRepository.saveAll(List.of(
                    new PlanCountry(1, "us"),
                    new PlanCountry(1, "ca"),
                    new PlanCountry(2, "uk"),
                    new PlanCountry(2, "fr"),
                    new PlanCountry(2, "es"),
                    new PlanCountry(2, "de"),
                    new PlanCountry(2, "it"),
                    new PlanCountry(1, "mx")
            ));
        }
    }

    private void seedPricePlanDetails() {
        if (pricePlanDetailRepository.count() == 0) {
            // Standard Pricing
            createPriceDetail(1, 1, "0g", "499g", 1.99);
            createPriceDetail(1, 2, "500g", "999g", 2.99);
            createPriceDetail(1, 3, "1000g", "1999g", 4.99);
            createPriceDetail(1, 4, "2000g", "2999g", 7.99);

            // Premium Pricing
            createPriceDetail(2, 1, "0g", "499g", 4.99);
            createPriceDetail(2, 2, "500g", "999g", 6.99);
            createPriceDetail(2, 3, "1000g", "1999g", 9.99);
            createPriceDetail(2, 4, "2000g", "2999g", 14.99);
        }
    }

    private void createPriceDetail(Integer pricingPlanId, Integer detailId,
                                   String fromWeight, String toWeight, Double price) {
        PlanPriceDetails detail = new PlanPriceDetails(
                pricingPlanId,
                detailId,
                fromWeight,
                toWeight,
                price
        );
        pricePlanDetailRepository.save(detail);
    }

    private void seedStaticShipmentDetails() {
        // Created for Testing
        shipmentDetailsService.testGenerateLabelAsyncWithStaticData();
    }
}
