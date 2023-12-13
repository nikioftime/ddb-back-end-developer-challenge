package com.ddbbackenddevchallenge.hitpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ddbbackenddevchallenge.hitpoints.database.DefensesRepository;
import com.ddbbackenddevchallenge.hitpoints.database.HealthRepository;
import com.ddbbackenddevchallenge.hitpoints.model.api.request.DamageRequest;
import com.ddbbackenddevchallenge.hitpoints.model.api.request.HealRequest;
import com.ddbbackenddevchallenge.hitpoints.model.api.request.TempHPRequest;
import com.ddbbackenddevchallenge.hitpoints.model.data.DefenseType;
import com.ddbbackenddevchallenge.hitpoints.model.data.Defenses;
import com.ddbbackenddevchallenge.hitpoints.model.data.Health;

@RestController
public class HitPointsController {
    private final HealthRepository healthRepository;
    private final DefensesRepository defensesRepository;

    public HitPointsController(HealthRepository healthRepository, DefensesRepository defensesRepository) {
        this.healthRepository = healthRepository;
        this.defensesRepository = defensesRepository;
    }

    @GetMapping("/health/{name}")
    public Health getHealth(@PathVariable String name) {
        return healthRepository.findById(name).orElseThrow();
    }

    @PostMapping("/heal")
    public Health heal(@RequestBody HealRequest healRequest) {
        Health creatureHealth = healthRepository.findById(healRequest.getName()).orElseThrow();

        final int healAmount = healRequest.getHealAmount();
        final int currentHealth = creatureHealth.getCurrentHitPoints();

        // Ensures creature HP does not exceed its max health
        final int healthAfterHeal = Math.min(currentHealth + healAmount, creatureHealth.getMaxHitPoints());

        creatureHealth.setCurrentHitPoints(healthAfterHeal);

        healthRepository.save(creatureHealth);

        return creatureHealth;
    }

    @PostMapping("/damage")
    public Health damage(@RequestBody DamageRequest damageRequest) {
        Health creatureHealth = healthRepository.findById(damageRequest.getName()).orElseThrow();
        Defenses creatureDefenses = defensesRepository.findById(damageRequest.getName()).orElseThrow();

        final DefenseType defenseType = creatureDefenses.getDefenseForDamageType(damageRequest.getDamageType());
        int damageTotal = damageRequest.getDamageAmount();

        if (defenseType.equals(DefenseType.IMMUNITY)) {
            // no damage taken
            return creatureHealth;
        } else if (defenseType.equals(DefenseType.RESISTANCE)) {
            // integer division rounds down, and HP is always an integer
            damageTotal /= 2;
        }

        // Handle Temporary HP
        int temporaryHitPoints = creatureHealth.getTemporaryHitPoints();

        if (temporaryHitPoints > damageTotal) {
            creatureHealth.setTemporaryHitPoints(temporaryHitPoints - damageTotal);
            damageTotal = 0;
        } else {
            damageTotal -= temporaryHitPoints;
            creatureHealth.setTemporaryHitPoints(0);
        }

        // Ensures creature never has negative hit points
        final int healthAfterDamage = Math.max(creatureHealth.getCurrentHitPoints() - damageTotal, 0);

        creatureHealth.setCurrentHitPoints(healthAfterDamage);

        healthRepository.save(creatureHealth);

        return creatureHealth;
    }

    @PostMapping("/tempHP")
    public Health addTemporaryHitPoints(@RequestBody TempHPRequest tempHPRequest) {
        Health creatureHealth = healthRepository.findById(tempHPRequest.getName()).orElseThrow();

        creatureHealth.setTemporaryHitPoints(
            Math.max(tempHPRequest.getTempHPAmount(), creatureHealth.getTemporaryHitPoints()));

        healthRepository.save(creatureHealth);

        return creatureHealth;
    }
    
}
