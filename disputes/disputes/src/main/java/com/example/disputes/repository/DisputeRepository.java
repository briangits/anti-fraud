package com.example.disputes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.disputes.entity.Dispute;
@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long>{
    
}
