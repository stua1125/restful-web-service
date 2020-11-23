package com.kakaopay.restfulwebservice.repository;

import com.kakaopay.restfulwebservice.models.Receive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiveRepository extends JpaRepository<Receive, String> {
     Optional<Receive> findByTokenValue(String tokenValue);
}