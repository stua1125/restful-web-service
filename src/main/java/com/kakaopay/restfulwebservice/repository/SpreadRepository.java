package com.kakaopay.restfulwebservice.repository;

import com.kakaopay.restfulwebservice.models.Receive;
import com.kakaopay.restfulwebservice.models.Spread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpreadRepository extends JpaRepository<Spread, String> {
    Optional<Spread> findByToken(String token);
}