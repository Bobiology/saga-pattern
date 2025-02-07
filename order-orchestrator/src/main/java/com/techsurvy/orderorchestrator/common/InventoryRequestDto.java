package com.techsurvy.orderorchestrator.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class InventoryRequestDto {
    private Integer userId;
    private Integer productId;
    private UUID orderId;
}
