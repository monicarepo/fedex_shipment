package com.example.fedex.security.shipment;

import com.example.fedex.entity.shipment.PlanCountry;
import com.example.fedex.repository.PricePlanCountryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final PricePlanCountryRepository planCountryRepository;

    public DataSeeder(PricePlanCountryRepository planCountryRepository) {
        this.planCountryRepository = planCountryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (planCountryRepository.count() == 0) {
            planCountryRepository.saveAll(List.of(
                    new PlanCountry(1, 1, "us"),
                    new PlanCountry(2, 1, "ca"),
                    new PlanCountry(3, 2, "uk"),
                    new PlanCountry(4, 2, "fr"),
                    new PlanCountry(5, 2, "es"),
                    new PlanCountry(6, 2, "de"),
                    new PlanCountry(7, 2, "it"),
                    new PlanCountry(8, 1, "mx")
            ));
        }
    }
}
