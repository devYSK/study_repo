package com.example.performance.controller;

import com.example.performance.service.SendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/send")
public class SendController {
    private final SendService sendService;

    public SendController(SendService sendService) {
        this.sendService = sendService;
    }

    @GetMapping("/logs")
    public ResponseEntity<Object> sendAll(){
        return new ResponseEntity<>(sendService.sendAll(), HttpStatus.OK);
    }


}
