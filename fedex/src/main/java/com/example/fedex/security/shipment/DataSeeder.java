package com.example.fedex.security.shipment;

import com.example.fedex.controller.AuthController;
import com.example.fedex.entity.*;
import com.example.fedex.entity.shipment.Member;
import com.example.fedex.entity.shipment.MembershipType;
import com.example.fedex.entity.shipment.PlanCountry;
import com.example.fedex.entity.shipment.PlanPriceDetails;
import com.example.fedex.repository.*;
import com.example.fedex.service.ShipmentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.fedex.entity.ERole.USER;

@Component
public class DataSeeder implements CommandLineRunner {
    private final PricePlanCountryRepository planCountryRepository;

    @Autowired
    private ShipmentDetailsService shipmentDetailsService;

    @Autowired
    private PricePlanDetailRepository pricePlanDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthController authController;

    @Autowired
    private ShipmentDetailsRepository shipmentDetailsRepository;

    public DataSeeder(PricePlanCountryRepository planCountryRepository) {
        this.planCountryRepository = planCountryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRole();
        seedUserDetails();
        seedPlanCountry();
        seedPricePlanDetails();
        seedStaticShipmentDetails();
        seedMembers();
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

    private void seedUserDetails(){
        if (userRepository.count() == 0) {
            createUser();
        }
    }

    private void seedRole(){
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.USER));
            roleRepository.save(new Role(ERole.ADMIN));
        }
    }

    private void createUser() {
        AuthController.SignUpInput input = new AuthController.SignUpInput(
                "monica",
                "monica.a@deemsysinc.com",
                "password123",
                Set.of(GraphQLRole.ADMIN)
        );

        SignUpResponse response = authController.signUp(input);
        System.out.println("Seeder Response: " + response.getMessage());
    }

    private void seedMembers(){
        if (memberRepository.count() == 0) {
            createMembers("Monica Amalanathan",
                    "Monica",
                    "Amalanathan",
                    "monica.a@deemsysinc.com",
                    "+1-740-972-0501",
                    MembershipType.BASIC
            );
        }
    }

    private void createMembers(String fullName, String firstName, String lastName, String email, String phoneNumber, MembershipType membershipType){
        long now = System.currentTimeMillis();
        Member member = new Member(
                fullName,
                firstName,
                lastName,
                email,
                phoneNumber,
                MembershipType.BASIC,
                new Date(now),
                "Active");
        memberRepository.save(member);
    }

    private void seedStaticShipmentDetails() {
        // Created for Testing
        if(shipmentDetailsRepository.count() == 0) {
            shipmentDetailsService.testGenerateLabelAsyncWithStaticData();
        }
    }
}
