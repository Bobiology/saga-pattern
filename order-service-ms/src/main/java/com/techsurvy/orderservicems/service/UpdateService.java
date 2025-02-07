package com.techsurvy.orderservicems.service;

import com.techsurvy.orderservicems.dto.OrchestratorResponseDto;
import com.techsurvy.orderservicems.repository.PurchaseOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UpdateService {
    private final PurchaseOrderRepository repo;

    @Autowired
    public UpdateService(PurchaseOrderRepository repo) {
        this.repo = repo;
    }

    public Mono<Void> updateOrder(OrchestratorResponseDto responseDTO) {
        log.info("Response::{}"+responseDTO.getStatus());

        return repo.findById(responseDTO.getOrderId())
                .doOnNext(p -> p.setStatus(responseDTO.getStatus()))
                .doOnNext(repo::save)
                .then();
    }
}
