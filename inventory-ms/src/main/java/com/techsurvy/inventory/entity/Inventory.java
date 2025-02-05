package com.techsurvy.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "inventory")
public class Inventory {
    private Integer userId;
    @Column
    private Integer productId;
    @Id
    @Column
    private UUID orderId;
}
