package com.techsurvy.inventory.service;

import com.techsurvy.inventory.dto.InventoryRequestDto;
import com.techsurvy.inventory.dto.InventoryResponseDto;
import com.techsurvy.inventory.dto.InventoryStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {
    private Map<Integer, Integer> inventoryMap;

    @PostConstruct
    private void init(){
        inventoryMap = new HashMap<>();
        inventoryMap.put(1, 2);
        inventoryMap.put(2, 3);
        inventoryMap.put(3, 4);
    }

    public InventoryResponseDto deduct(InventoryRequestDto requestDto){
        int qty = getQuantity(requestDto.getProductId());
        InventoryResponseDto responseDto = new InventoryResponseDto();

        responseDto.setProductId(requestDto.getProductId());
        responseDto.setOrderId(requestDto.getOrderId());
        responseDto.setUserId(responseDto.getUserId());
        responseDto.setStatus(InventoryStatus.UNAVAILABLE);

        if (qty > 0){
            responseDto.setStatus(InventoryStatus.AVAILABLE);
            inventoryMap.put(requestDto.getProductId(), qty - 1);
        }

        return responseDto;
    }

    public void add(InventoryRequestDto requestDto){
        inventoryMap.computeIfPresent(requestDto.getProductId(), (k,v) -> v + 1);
    }

    private int getQuantity(Integer productId){
        return inventoryMap.getOrDefault(productId,0);
    }
}
