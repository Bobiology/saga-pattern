package com.techsurvy.orderorchestrator.steps;

import com.techsurvy.orderorchestrator.common.InventoryRequestDto;
import com.techsurvy.orderorchestrator.common.InventoryResponseDto;
import com.techsurvy.orderorchestrator.common.InventoryStatus;
import com.techsurvy.orderorchestrator.service.WorkflowStep;
import com.techsurvy.orderorchestrator.service.WorkflowStepStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class InventoryStep implements WorkflowStep {

    private final WebClient webClient;
    private final InventoryRequestDto requestDTO;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public InventoryStep(WebClient webClient, InventoryRequestDto requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        return webClient
                .post()
                .uri("/inventory/deduct")
                .body(BodyInserters.fromValue(requestDTO))
                .retrieve()
                .bodyToMono(InventoryResponseDto.class)
                .map(r -> r.getStatus().equals(InventoryStatus.AVAILABLE))
                .doOnNext(b -> stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        return webClient
                .post()
                .uri("/inventory/add")
                .body(BodyInserters.fromValue(requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r -> true)
                .onErrorReturn(false);
    }
}
