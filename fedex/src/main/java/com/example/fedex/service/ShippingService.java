package com.example.fedex.service;

import com.example.fedex.dto.ShippingDetailResponse;
import com.example.fedex.entity.ShippingDetails;
import com.example.fedex.repository.ShippingDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShippingService {
    @Autowired
    private ShippingDetailsRepository shippingDetailsRepository;

    public List<ShippingDetailResponse> getAllShippingDetails() {
        return shippingDetailsRepository.findAll().stream()
                .map(ShippingDetailResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<ShippingDetailResponse> getShippingDetailById(Long id) {
        return shippingDetailsRepository.findById(id)
                .map(ShippingDetailResponse::new);
    }

    public Optional<ShippingDetailResponse> getShippingDetailsByTrackingNumber(String trackingNumber) {
        return shippingDetailsRepository.findByTrackingNumber(trackingNumber)
                .map(ShippingDetailResponse::new);
    }

    public ShippingDetailResponse createShippingDetail(ShippingDetailResponse shippingDetailsResponse) {
        ShippingDetails shippingDetail = new ShippingDetails();
        mapToEntity(shippingDetailsResponse, shippingDetail);
        ShippingDetails savedShippingDetail = shippingDetailsRepository.save(shippingDetail);
        return new ShippingDetailResponse(savedShippingDetail);
    }

    private void mapToEntity(ShippingDetailResponse dto, ShippingDetails entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setFromName(dto.getFromName());
        entity.setShippingAddress(dto.getShippingAddress());
        entity.setDeliveryMode(dto.getDeliveryMode());
        entity.setPrice(dto.getPrice());
    }

}
