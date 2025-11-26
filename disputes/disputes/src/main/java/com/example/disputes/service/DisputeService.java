package com.example.disputes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.disputes.entity.Dispute;
import com.example.disputes.repository.DisputeRepository;
import java.util.List;
import java.util.Map;



@Service
public class DisputeService {
    private final DisputeRepository repo;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;
     
    public DisputeService(DisputeRepository repo, RestTemplate restTemplate){
          this.repo=repo;
          this.restTemplate= restTemplate;
    }
    public Dispute saveDispute(Dispute dispute){
        return repo.save(dispute);
    }

    public List<Dispute> getAllDisputes(){
        return repo.findAll();
    }

    public Map getUserDetails(Long userId){
        return restTemplate.getForObject(userServiceUrl + "/" + userId, Map.class);
    }


}
