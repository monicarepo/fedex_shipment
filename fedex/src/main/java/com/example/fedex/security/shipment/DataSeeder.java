package com.example.fedex.security.shipment;

import com.example.fedex.entity.shipment.PlanCountry;
import com.example.fedex.repository.PricePlanCountryRepository;
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

    public DataSeeder(PricePlanCountryRepository planCountryRepository) {
        this.planCountryRepository = planCountryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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

        // Created for Testing
//        shipmentDetailsService.testGenerateLabelAsyncWithStaticData();
    }
}
