package com.techsurvy.orderorchestrator.config;

import com.techsurvy.orderorchestrator.common.OrchestratorRequestDto;
import com.techsurvy.orderorchestrator.common.OrchestratorResponseDto;
import com.techsurvy.orderorchestrator.service.OrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class OrchestratorConfig {

    @Autowired
    private OrchestratorService orchestratorService;

    @Bean
    public Function<Flux<OrchestratorRequestDto>, Flux<OrchestratorResponseDto>> processor() {
        return flux -> flux.flatMap(dto -> orchestratorService.orderProduct(dto))
                .doOnNext(dto -> System.out.println("Status : " + dto.getStatus()));
    }

}
