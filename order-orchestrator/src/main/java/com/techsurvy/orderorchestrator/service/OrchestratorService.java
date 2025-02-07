package com.techsurvy.orderorchestrator.service;

import com.techsurvy.orderorchestrator.common.InventoryRequestDto;
import com.techsurvy.orderorchestrator.common.OrchestratorRequestDto;
import com.techsurvy.orderorchestrator.common.OrchestratorResponseDto;
import com.techsurvy.orderorchestrator.common.OrderStatus;
import com.techsurvy.orderorchestrator.common.PaymentRequestDto;
import com.techsurvy.orderorchestrator.steps.InventoryStep;
import com.techsurvy.orderorchestrator.steps.PaymentStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrchestratorService {

    @Autowired
    @Qualifier("payment")
    private WebClient paymentClient;

    @Autowired
    @Qualifier("inventory")
    private WebClient inventoryClient;

    public Mono<OrchestratorResponseDto> orderProduct(final OrchestratorRequestDto requestDTO) {
        Workflow orderWorkflow = getOrderWorkflow(requestDTO);

        return Flux.fromStream(() -> orderWorkflow.getSteps().stream()).flatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if (aBoolean.booleanValue()) {
                        synchronousSink.next(true);
                    } else {
                        synchronousSink.error(new WorkflowException("Order not processed."));
                    }
                })).then(Mono.fromCallable(() -> getResponseDTO(requestDTO, OrderStatus.ORDER_COMPLETED)))
                .onErrorResume(ex -> revertOrder(orderWorkflow, requestDTO));

    }

    private Mono<OrchestratorResponseDto> revertOrder(final Workflow workflow, final OrchestratorRequestDto requestDTO) {
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkflowStep::revert).retry(3)
                .then(Mono.just(getResponseDTO(requestDTO, OrderStatus.ORDER_CANCELLED)));
    }

    private Workflow getOrderWorkflow(OrchestratorRequestDto requestDTO) {
        WorkflowStep paymentStep = new PaymentStep(paymentClient, getPaymentRequestDTO(requestDTO));
        WorkflowStep inventoryStep = new InventoryStep(inventoryClient, getInventoryRequestDTO(requestDTO));
        return new OrderWorkflow(List.of(paymentStep, inventoryStep));
    }

    private OrchestratorResponseDto getResponseDTO(OrchestratorRequestDto requestDTO, OrderStatus status) {
        OrchestratorResponseDto responseDTO = new OrchestratorResponseDto();
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setAmount(requestDTO.getAmount());
        responseDTO.setProductId(requestDTO.getProductId());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setStatus(status);
        return responseDTO;
    }

    private PaymentRequestDto getPaymentRequestDTO(OrchestratorRequestDto requestDTO) {
        PaymentRequestDto paymentRequestDTO = new PaymentRequestDto();
        paymentRequestDTO.setUserId(requestDTO.getUserId());
        paymentRequestDTO.setAmount(requestDTO.getAmount());
        paymentRequestDTO.setOrderId(requestDTO.getOrderId());
        return paymentRequestDTO;
    }

    private InventoryRequestDto getInventoryRequestDTO(OrchestratorRequestDto requestDTO) {
        InventoryRequestDto inventoryRequestDTO = new InventoryRequestDto();
        inventoryRequestDTO.setUserId(requestDTO.getUserId());
        inventoryRequestDTO.setProductId(requestDTO.getProductId());
        inventoryRequestDTO.setOrderId(requestDTO.getOrderId());
        return inventoryRequestDTO;
    }

}
