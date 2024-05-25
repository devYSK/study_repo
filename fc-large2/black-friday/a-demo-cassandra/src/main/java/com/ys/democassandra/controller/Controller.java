package com.ys.democassandra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ys.democassandra.service.CassandraService;

@RestController
public class Controller {
    @Autowired
    CassandraService cassandraService;

    @GetMapping("/cas-test")
    public void cassandraTest() {
        cassandraService.casTest();
    }
}
