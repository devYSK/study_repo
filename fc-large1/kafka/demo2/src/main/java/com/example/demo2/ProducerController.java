package com.example.demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    private KafkaProduceService kafkaProduceService;

    @GetMapping("/publish")
    public String publish(
        @RequestParam String message) {
        kafkaProduceService.send(message);
        return "published a message :" + message;
    }

    @GetMapping("/publish2")
    public String publishWithCallback(@RequestParam String message) {
        kafkaProduceService.sendWithCallback(message);
        return "published a message with callback :" + message;
    }

    @GetMapping("/publish3")
    public String publishJson(@ModelAttribute MyMessage message) {
        kafkaProduceService.sendJson(message);
        return "published a message with callback :" + message.getName() + "," + message.getMessage();
    }

}
