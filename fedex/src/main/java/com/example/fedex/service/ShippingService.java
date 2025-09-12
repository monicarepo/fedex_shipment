package com.example.fedex.service;

import com.example.fedex.dto.ShippingDetailResponse;
import com.example.fedex.entity.DeliveryMode;
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
        entity.setSenderFirstName(dto.getSenderFirstName());
        entity.setSenderLastName(dto.getSenderLastName());
        entity.setSenderEmail(dto.getSenderEmail());
        entity.setSenderContactNumber(dto.getSenderContactNumber());
        entity.setSenderAddress(dto.getSenderAddress());
        entity.setReceiverFirstName(dto.getReceiverFirstName());
        entity.setReceiverLastName(dto.getReceiverLastName());
        entity.setReceiverEmail(dto.getReceiverEmail());
        entity.setReceiverContactNumber(dto.getReceiverContactNumber());
        entity.setReceiverAddress(dto.getReceiverAddress());
        entity.setDeliveryMode(dto.getDeliveryMode());
        entity.setShippingStatus(dto.getShippingStatus());
        entity.setPrice(dto.getPrice());
    }

    public Optional<ShippingDetailResponse> updateShippingDetails(Long id, ShippingDetailResponse shippingDetailResponse) {
        return shippingDetailsRepository.findById(id)
                .map(existingDetails -> {
                        mapToEntity(shippingDetailResponse, existingDetails);
                        ShippingDetails updatedDetails = shippingDetailsRepository.save(existingDetails);
                        return new ShippingDetailResponse(updatedDetails);
                });
    }

    public boolean deleteShippingDetails(Long id) {
        if (shippingDetailsRepository.existsById(id)) {
            shippingDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ShippingDetailResponse> getDeliveryMode(DeliveryMode deliveryMode) {
        return shippingDetailsRepository.findByDeliveryMode(deliveryMode)
                .stream()
                .map(ShippingDetailResponse::new)
                .collect(Collectors.toList());
    }

}
