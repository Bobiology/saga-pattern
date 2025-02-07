package com.techsurvy.orderservicems.service;

import com.techsurvy.orderservicems.dto.OrchestratorRequestDto;
import com.techsurvy.orderservicems.dto.OrderRequestDto;
import com.techsurvy.orderservicems.dto.OrderResponseDto;
import com.techsurvy.orderservicems.dto.OrderStatus;
import com.techsurvy.orderservicems.entity.PurchaseOrder;
import com.techsurvy.orderservicems.repository.PurchaseOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;

@Slf4j
@Service
public class OrderService {
    private static final Map<Integer, Double> ORDER_PRICE = Map.of(
            1, 100d,
            2, 200d,
            3, 300d
    );

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final Sinks.Many<OrchestratorRequestDto> sink;

    @Autowired
    public OrderService(PurchaseOrderRepository purchaseOrderRepository, Sinks.Many<OrchestratorRequestDto> sink) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.sink = sink;
    }

    public Mono<PurchaseOrder> createOrder(OrderRequestDto orderRequestDto){
        return purchaseOrderRepository.save(dtoToEntity(orderRequestDto))
                .doOnNext(e -> orderRequestDto.setOrderId(e.getId()))
                .doOnNext(e -> emitEvent(orderRequestDto));
    }

    public Flux<OrderResponseDto> getAllOrder(){
        return purchaseOrderRepository.findAll()
                .map(this::entityToDto);
    }

    private void emitEvent(OrderRequestDto orderRequestDto){
        sink.tryEmitNext(getOrchestratorRequestDto(orderRequestDto));
    }

    private OrchestratorRequestDto getOrchestratorRequestDto(OrderRequestDto orderRequestDto) {
        OrchestratorRequestDto requestDto = new OrchestratorRequestDto();
        requestDto.setUserId(orderRequestDto.getUserId());
        requestDto.setAmount(ORDER_PRICE.get(orderRequestDto.getProductId()));
        requestDto.setOrderId(orderRequestDto.getOrderId());
        requestDto.setProductId(orderRequestDto.getProductId());
        return requestDto;
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDto orderRequestDto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(orderRequestDto.getProductId());
        purchaseOrder.setUserId(orderRequestDto.getUserId());
        purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(ORDER_PRICE.get(purchaseOrder.getProductId()));
        purchaseOrder.setId(purchaseOrder.getId());

        return purchaseOrder;
    }

    private OrderResponseDto entityToDto(PurchaseOrder purchaseOrder) {
        log.info("Purchase Order Status::{}",purchaseOrder.getStatus());
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(purchaseOrder.getId());
        dto.setProductId(purchaseOrder.getProductId());
        dto.setUserId(purchaseOrder.getUserId());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setAmount(purchaseOrder.getPrice());
        return dto;
    }
}
