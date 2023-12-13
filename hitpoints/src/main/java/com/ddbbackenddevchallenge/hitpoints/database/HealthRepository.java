package com.ddbbackenddevchallenge.hitpoints.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddbbackenddevchallenge.hitpoints.model.data.Health;

public interface HealthRepository extends JpaRepository<Health, String> {
    

}