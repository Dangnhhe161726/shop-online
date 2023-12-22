package com.shopping.online.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}")
public class TestController {

    @GetMapping("/customers/")
    public String testRoleCustomer(){
        return "hello Customer";
    }

    @GetMapping("/sales/")
    public String testRoleSale(){
        return "hello Sale";
    }
}
