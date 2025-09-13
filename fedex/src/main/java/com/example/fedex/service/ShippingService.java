package com.example.fedex.service;

import com.example.fedex.dto.ShippingDetailResponse;
import com.example.fedex.entity.DeliveryMode;
import com.example.fedex.entity.ShippingDetails;
import com.example.fedex.repository.ShippingDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShippingService {
    @Autowired
    private ShippingDetailsRepository shippingDetailsRepository;

    @Cacheable(value = "shippingDetails", key = "'all'")
    public List<ShippingDetailResponse> getAllShippingDetails() {
        return shippingDetailsRepository.findAll().stream()
                .map(ShippingDetailResponse::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "shippingDetails", key = "'id:' + #id")
    public Optional<ShippingDetailResponse> getShippingDetailById(Long id) {
        return shippingDetailsRepository.findById(id)
                .map(ShippingDetailResponse::new);
    }

    @Cacheable(value = "shippingDetails", key = "'tracking:' + #trackingNumber")
    public Optional<ShippingDetailResponse> getShippingDetailsByTrackingNumber(String trackingNumber) {
        return shippingDetailsRepository.findByTrackingNumber(trackingNumber)
                .map(ShippingDetailResponse::new);
    }

    @Caching(evict = {
        @CacheEvict(value = "shippingDetails", key = "'all'", beforeInvocation = true),
        @CacheEvict(value = "shippingDetails", key = "'deliveryMode:*'", allEntries = true, beforeInvocation = true)
    })
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

    @Caching(evict = {
        @CacheEvict(value = "shippingDetails", key = "'id:' + #id", beforeInvocation = true),
        @CacheEvict(value = "shippingDetails", key = "'all'", beforeInvocation = true),
        @CacheEvict(value = "shippingDetails", key = "'deliveryMode:*'", allEntries = true, beforeInvocation = true)
    })
    public Optional<ShippingDetailResponse> updateShippingDetails(Long id, ShippingDetailResponse shippingDetailResponse) {
        return shippingDetailsRepository.findById(id)
                .map(existingDetails -> {
                        mapToEntity(shippingDetailResponse, existingDetails);
                        ShippingDetails updatedDetails = shippingDetailsRepository.save(existingDetails);
                        return new ShippingDetailResponse(updatedDetails);
                });
    }

    @Caching(evict = {
        @CacheEvict(value = "shippingDetails", key = "'id:' + #id", beforeInvocation = true),
        @CacheEvict(value = "shippingDetails", key = "'all'", beforeInvocation = true),
        @CacheEvict(value = "shippingDetails", key = "'deliveryMode:*'", allEntries = true, beforeInvocation = true)
    })
    public boolean deleteShippingDetails(Long id) {
        if (shippingDetailsRepository.existsById(id)) {
            shippingDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Cacheable(value = "shippingDetails", key = "'deliveryMode:' + #deliveryMode")
    public List<ShippingDetailResponse> getDeliveryMode(DeliveryMode deliveryMode) {
        return shippingDetailsRepository.findByDeliveryMode(deliveryMode)
                .stream()
                .map(ShippingDetailResponse::new)
                .collect(Collectors.toList());
    }

}
