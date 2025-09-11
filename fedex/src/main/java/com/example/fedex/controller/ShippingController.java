package com.example.fedex.controller;

import com.example.fedex.dto.ShippingDetailResponse;
import com.example.fedex.entity.DeliveryMode;
import com.example.fedex.repository.ShippingDetailsRepository;
import com.example.fedex.service.ShippingService;
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
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ShippingDetailResponse> allShippingDetails() {
        return shippingService.getAllShippingDetails();
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<ShippingDetailResponse> shippingDetailById(@Argument Long id) {
        return shippingService.getShippingDetailById(id);
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<ShippingDetailResponse> shippingDetailsByTrackingNumber(@Argument String trackingNumber) {
        return shippingService.getShippingDetailsByTrackingNumber(trackingNumber);
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ShippingDetailResponse> searchShippingByFirstName(@Argument String firstName) {
        return shippingService.searchByFirstName(firstName);
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ShippingDetailResponse> shippingByDeliveryMode(@Argument DeliveryMode deliveryMode) {
        return shippingService.getDeliveryMode(deliveryMode);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ShippingDetailResponse createShippingDetails(@Argument @Valid ShippingInput input) {
        ShippingDetailResponse dto = new ShippingDetailResponse();
        dto.setFirstName(input.firstName);
        dto.setLastName(input.lastName);
        dto.setFromName(input.fromName);
        dto.setContactNumber(input.contactNumber);
        dto.setShippingAddress(input.shippingAddress);
        dto.setDeliveryMode(input.deliveryMode);
        dto.setPrice(input.price);
        return shippingService.createShippingDetail(dto);
    }


    public record ShippingInput(
        String firstName,
        String lastName,
        String fromName,
        String contactNumber,
        String shippingAddress,
        DeliveryMode deliveryMode,
        Double price
    ) {}

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<ShippingDetailResponse> updateShippingDetails(@Argument Long id, @Argument @Valid ShippingInput input) {
        ShippingDetailResponse dto = new ShippingDetailResponse();
        dto.setFirstName(input.firstName);
        dto.setLastName(input.lastName);
        dto.setFromName(input.fromName);
        dto.setContactNumber(input.contactNumber);
        dto.setShippingAddress(input.shippingAddress);
        dto.setDeliveryMode(input.deliveryMode);
        dto.setPrice(input.price);
        return shippingService.updateShippingDetails(id, dto);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteShippingDetails(@Argument Long id) {
        return shippingService.deleteShippingDetails(id);
    }

}
