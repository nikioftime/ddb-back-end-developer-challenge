package com.ddbbackenddevchallenge.hitpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ddbbackenddevchallenge.hitpoints.database.DefensesRepository;
import com.ddbbackenddevchallenge.hitpoints.database.HealthRepository;
import com.ddbbackenddevchallenge.hitpoints.model.api.exceptions.CreatureNotFoundException;
import com.ddbbackenddevchallenge.hitpoints.model.api.request.DamageRequest;
import com.ddbbackenddevchallenge.hitpoints.model.api.request.HealRequest;
import com.ddbbackenddevchallenge.hitpoints.model.api.request.TempHPRequest;
import com.ddbbackenddevchallenge.hitpoints.model.data.DefenseType;
import com.ddbbackenddevchallenge.hitpoints.model.data.Defenses;
import com.ddbbackenddevchallenge.hitpoints.model.data.Health;
import com.google.common.base.Preconditions;

@RestController
public class HitPointsController {
    private static final Logger log = LoggerFactory.getLogger(HitPointsController.class);

    private final HealthRepository healthRepository;
    private final DefensesRepository defensesRepository;

    public HitPointsController(HealthRepository healthRepository, DefensesRepository defensesRepository) {
        this.healthRepository = healthRepository;
        this.defensesRepository = defensesRepository;
    }

    
    /**
     * API to view a creature's existing health. Not included in the spec, but added for convenience
     * 
     * @param name
     * @return Object representing creature's current, temporary, and maximum hit points
     */
    @GetMapping("/health/{name}")
    public Health getHealth(@PathVariable String name) {
        Preconditions.checkNotNull(name);
        return healthRepository.findById(name)
            .orElseThrow(() -> new CreatureNotFoundException(name));
    }

    /**
     * API to heal a particular creature. Healing can never increase current HP beyond its maximum.
     * 
     * @param healRequest
     * @return Object representing creature's current, temporary, and maximum hit points following the heal
     */
    @PostMapping("/heal")
    public Health heal(@RequestBody HealRequest healRequest) {
        Preconditions.checkNotNull(healRequest);
        Preconditions.checkNotNull(healRequest.getName());
        Preconditions.checkNotNull(healRequest.getHealAmount());
        Preconditions.checkArgument(healRequest.getHealAmount() >= 0, 
            "Heal amount must be non-negative");

        Health creatureHealth = healthRepository.findById(healRequest.getName())
            .orElseThrow(() -> new CreatureNotFoundException(healRequest.getName()));

        final int healAmount = healRequest.getHealAmount();
        final int currentHealth = creatureHealth.getCurrentHitPoints();

        // Ensures creature HP does not exceed its max health
        final int healthAfterHeal = Math.min(currentHealth + healAmount, creatureHealth.getMaxHitPoints());

        creatureHealth.setCurrentHitPoints(healthAfterHeal);

        healthRepository.save(creatureHealth);

        log.info("Creature %s was healed for %d hit points. Creature health after heal: %s", 
            healRequest.getName(), healRequest.getHealAmount(), creatureHealth.toString());

        return creatureHealth;
    }

    /**
     * API to deal damage of a particular type to a creature.
     * Damage cannot drop a creature's HP below 0.
     * 
     * A creature having an immunity to a particular type of damage means that their current health will not change.
     * 
     * A creature having a resistance to a particular type of damage will take half of the damage amout, rounded down.
     * 
     * @param damageRequest
     * @return Object representing creature's current, temporary, and maximum hit points
     */
    @PostMapping("/damage")
    public Health damage(@RequestBody DamageRequest damageRequest) {
        Preconditions.checkNotNull(damageRequest);
        Preconditions.checkNotNull(damageRequest.getName());
        Preconditions.checkNotNull(damageRequest.getDamageType());
        Preconditions.checkNotNull(damageRequest.getDamageAmount());
        Preconditions.checkArgument(damageRequest.getDamageAmount() >= 0, 
            "Damage Amount must be non-negative");

        Health creatureHealth = healthRepository.findById(damageRequest.getName())
            .orElseThrow(() -> new CreatureNotFoundException(damageRequest.getName()));
        Defenses creatureDefenses = defensesRepository.findById(damageRequest.getName())
            .orElseThrow(() -> new CreatureNotFoundException(damageRequest.getName()));

        final DefenseType defenseType = creatureDefenses.getDefenseForDamageType(damageRequest.getDamageType());
        int damageTotal = damageRequest.getDamageAmount();

        if (defenseType.equals(DefenseType.IMMUNITY)) {
            // no damage taken
            return creatureHealth;
        } else if (defenseType.equals(DefenseType.RESISTANCE)) {
            // HP is always an integer, 5e rounds down to make things integers, and integer division rounds down
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

        log.info("Creature %s was hit for %d hit points of %s damage. Creature health after damage: %s", 
            damageRequest.getName(), damageTotal, damageRequest.getDamageType().toString(), 
            creatureHealth.toString());

        return creatureHealth;
    }

    /**
     * Adds temporary HP to a creature.
     * 
     * Temp HP is not additive, the temp HP in the request will only replace the creatures temporary HP 
     * if it exceeds its existing temp HP 
     * 
     * @param tempHPRequest
     * @return Object representing creature's current, temporary, and maximum hit points
     */
    @PostMapping("/tempHP")
    public Health addTemporaryHitPoints(@RequestBody TempHPRequest tempHPRequest) {
        Preconditions.checkNotNull(tempHPRequest);
        Preconditions.checkNotNull(tempHPRequest.getName());
        Preconditions.checkNotNull(tempHPRequest.getTempHPAmount());
        Preconditions.checkArgument(tempHPRequest.getTempHPAmount() >= 0, 
            "Temporary HP Amount must be non-negative");

        Health creatureHealth = healthRepository.findById(tempHPRequest.getName())
            .orElseThrow(() -> new CreatureNotFoundException(tempHPRequest.getName()));

        creatureHealth.setTemporaryHitPoints(
            Math.max(tempHPRequest.getTempHPAmount(), creatureHealth.getTemporaryHitPoints()));

        healthRepository.save(creatureHealth);

        log.info("Creature %s was attempting to gain %d temp HP. Creature health after temp HP granted: %s", 
            tempHPRequest.getName(), tempHPRequest.getTempHPAmount(), creatureHealth.toString());

        return creatureHealth;
    }
    
}
