package com.techsurvy.orderservicems.consumer;

import com.techsurvy.orderservicems.dto.OrchestratorRequestDto;
import com.techsurvy.orderservicems.dto.OrchestratorResponseDto;
import com.techsurvy.orderservicems.service.UpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;
@Slf4j
@Configuration
public class OrderConsumer {

    @Autowired
    private Flux<OrchestratorRequestDto> flux;

    @Autowired
    private UpdateService update;

    @Bean
    public Supplier<Flux<OrchestratorRequestDto>> supplier() {
        return () -> flux;
    }

    @Bean
    public Consumer<Flux<OrchestratorResponseDto>> consumer() {
        return c -> c
                .doOnNext(a -> log.info("Consuming::{}", a))
                .flatMap(responseDTO -> update.updateOrder(responseDTO))
                .subscribe();
    }

}
