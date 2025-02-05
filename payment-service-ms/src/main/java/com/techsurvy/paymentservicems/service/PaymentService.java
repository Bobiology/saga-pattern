package com.techsurvy.paymentservicems.service;

import com.techsurvy.paymentservicems.dto.PaymentRequestDto;
import com.techsurvy.paymentservicems.dto.PaymentResponseDto;
import com.techsurvy.paymentservicems.dto.PaymentStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    private Map<Integer, Double> paymentMap;

    @PostConstruct
    private void init(){
        paymentMap = new HashMap<>();
        paymentMap.put(1, 500d);
        paymentMap.put(2, 1000d);
        paymentMap.put(3, 700d);
    }

    public PaymentResponseDto debit(PaymentRequestDto requestDto){
        double balance = getAmount(requestDto.getUserId());
        PaymentResponseDto responseDto = new PaymentResponseDto();

        responseDto.setOrderId(requestDto.getOrderId());
        responseDto.setUserId(responseDto.getUserId());
        responseDto.setAmount(responseDto.getAmount());

        responseDto.setStatus(PaymentStatus.PAYMENT_REJECTED);

        if (balance >= requestDto.getAmount()){
            responseDto.setStatus(PaymentStatus.PAYMENT_APPROVED);
            paymentMap.put(requestDto.getUserId(), balance - requestDto.getAmount());
        }

        return responseDto;
    }

    public void credit(PaymentRequestDto requestDto){
        paymentMap.computeIfPresent(requestDto.getUserId(), (k,v) -> v + requestDto.getAmount());
    }

    private Double getAmount(Integer userId){
        return paymentMap.getOrDefault(userId,0d);
    }

}
