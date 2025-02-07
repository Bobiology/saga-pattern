package com.techsurvy.orderservicems.controller;

import com.techsurvy.orderservicems.dto.OrderRequestDto;
import com.techsurvy.orderservicems.dto.OrderResponseDto;
import com.techsurvy.orderservicems.entity.PurchaseOrder;
import com.techsurvy.orderservicems.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Mono<PurchaseOrder>> createOrder(@RequestBody Mono<OrderRequestDto> orderMono){
        Mono<PurchaseOrder> response = orderMono.flatMap(orderService::createOrder);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<OrderResponseDto>> getOrders(){
        Flux<OrderResponseDto> response = orderService.getAllOrder();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
