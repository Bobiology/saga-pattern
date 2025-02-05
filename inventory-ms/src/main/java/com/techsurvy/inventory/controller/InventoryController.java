package com.techsurvy.inventory.controller;

import com.techsurvy.inventory.dto.InventoryRequestDto;
import com.techsurvy.inventory.dto.InventoryResponseDto;
import com.techsurvy.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;
    @PostMapping("/deduct")
    public ResponseEntity<InventoryResponseDto> deduct(@RequestBody InventoryRequestDto requestDto){
        InventoryResponseDto responseDto = inventoryService.deduct(requestDto);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<InventoryResponseDto> add(@RequestBody InventoryRequestDto requestDto){
        inventoryService.add(requestDto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
