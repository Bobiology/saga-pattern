package com.techsurvy.inventory.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @PostMapping("/deduct")
    public void deduct(){

    }

    @PostMapping("/add")
    public void add(){

    }
}
