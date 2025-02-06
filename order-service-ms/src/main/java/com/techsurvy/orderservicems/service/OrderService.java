package com.techsurvy.orderservicems.service;

import com.techsurvy.orderservicems.dto.OrchestratorRequestDto;
import com.techsurvy.orderservicems.dto.OrderRequestDto;
import com.techsurvy.orderservicems.dto.OrderStatus;
import com.techsurvy.orderservicems.entity.PurchaseOrder;
import com.techsurvy.orderservicems.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;

@Service
public class OrderService {
    private static final Map<Integer, Double> ORDER_PRICE = Map.of(
            1, 100d,
            2, 200d,
            3, 300d
    );

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final Sinks.Many<OrchestratorRequestDto> sink;

    public OrderService(PurchaseOrderRepository purchaseOrderRepository, Sinks.Many<OrchestratorRequestDto> sink) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.sink = sink;
    }

    public Mono<PurchaseOrder> createOrder(OrderRequestDto orderRequestDto){
        purchaseOrderRepository.save(dtoToEntity(orderRequestDto))
                .doOnNext(e -> orderRequestDto.setOrderId(e.getId()))
    }

    private void emitEvent(OrderRequestDto orderRequestDto){
        sink.tryEmitNext(getOrchestratorRequestDto(orderRequestDto));
    }

    private OrchestratorRequestDto getOrchestratorRequestDto(OrderRequestDto orderRequestDto) {

        return null;
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
}
