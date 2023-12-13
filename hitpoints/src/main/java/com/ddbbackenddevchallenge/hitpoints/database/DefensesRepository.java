package com.ddbbackenddevchallenge.hitpoints.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddbbackenddevchallenge.hitpoints.model.data.Defenses;

public interface DefensesRepository extends JpaRepository<Defenses, String> {
    
}
