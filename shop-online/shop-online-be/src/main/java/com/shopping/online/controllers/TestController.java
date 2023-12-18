package com.shopping.online.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @GetMapping("/customer/")
    public String testRoleCustomer(){
        return "hello Customer";
    }

    @GetMapping("/sale/")
    public String testRoleSale(){
        return "hello Sale";
    }
}
