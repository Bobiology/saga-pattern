package com.techsurvy.paymentservicems.controller;

import com.techsurvy.paymentservicems.dto.PaymentRequestDto;
import com.techsurvy.paymentservicems.dto.PaymentResponseDto;
import com.techsurvy.paymentservicems.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @PostMapping("/credit")
    public ResponseEntity<PaymentResponseDto> credit(@RequestBody PaymentRequestDto paymentRequestDto){
        paymentService.credit(paymentRequestDto);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    @PostMapping("/debit")
    public ResponseEntity<PaymentResponseDto> debit(@RequestBody PaymentRequestDto paymentRequestDto){
        PaymentResponseDto responseDto = paymentService.debit(paymentRequestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
