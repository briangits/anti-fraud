package com.example.disputes.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.example.disputes.entity.Dispute;
import com.example.disputes.service.DisputeService;


@RestController
@RequestMapping("/disputes")
public class DisputeController {
    private final DisputeService service;
    
    public DisputeController(DisputeService service){
        this.service=service;
    }
      @PostMapping
    public Dispute createDispute(@RequestBody Dispute dispute){
        return service.saveDispute(dispute);

    }
    @GetMapping
     public List<Dispute> getAllDisputes(){
        return service.getAllDisputes();
       

    } 
    @GetMapping("/user/{userId}")
    public Map getUserDetails(@PathVariable Long userId){
        return service.getUserDetails(userId);
    }

}
