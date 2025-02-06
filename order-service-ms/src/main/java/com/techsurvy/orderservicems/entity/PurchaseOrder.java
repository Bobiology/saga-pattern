package com.techsurvy.orderservicems.entity;

import com.techsurvy.orderservicems.dto.OrderStatus;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class PurchaseOrder {
    @Id
    private UUID id;
    private Integer userId;
    private Integer productId;
    private Double price;
    private OrderStatus status;
}
