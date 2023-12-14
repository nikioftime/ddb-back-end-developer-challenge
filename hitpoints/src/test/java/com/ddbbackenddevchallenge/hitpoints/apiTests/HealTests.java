package com.ddbbackenddevchallenge.hitpoints.apiTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.ddbbackenddevchallenge.hitpoints.HitPointsController;
import com.ddbbackenddevchallenge.hitpoints.database.DefensesRepository;
import com.ddbbackenddevchallenge.hitpoints.database.HealthRepository;
import com.ddbbackenddevchallenge.hitpoints.model.api.exceptions.CreatureNotFoundException;
import com.ddbbackenddevchallenge.hitpoints.model.api.request.HealRequest;
import com.ddbbackenddevchallenge.hitpoints.model.data.Health;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HealTests {

    private static final String BRIV_NAME = "Briv";
    private static final String CREATURE_NOT_EXIST_NAME = "Reeza";
    private static final int BRIV_MAX_HEALTH = 25;
    private static final Health BRIV_STARTING_HEALTH = new Health(BRIV_NAME, BRIV_MAX_HEALTH);
    @Mock
    private HealthRepository healthRepository;
    @Mock
    private DefensesRepository defensesRepository;

    private HitPointsController hitPointsController;

    @BeforeEach
    void setup() {
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(BRIV_STARTING_HEALTH));
        when(healthRepository.findById(CREATURE_NOT_EXIST_NAME)).thenReturn(Optional.empty());

        this.hitPointsController = new HitPointsController(this.healthRepository, this.defensesRepository);
    }

    @Test
    void testHeal_increasesCorrectAmount() {
        final int startingCurrentHealth = 5;
        final int healAmount = 10; 
        final Health startingHealth = new Health(BRIV_NAME, BRIV_MAX_HEALTH, startingCurrentHealth);

        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(startingHealth));

        final HealRequest request = new HealRequest(BRIV_NAME, healAmount);

        final Health endingHealth = this.hitPointsController.heal(request);

        final int expectedCurrentHealthAfterHeal = startingCurrentHealth + healAmount;

        assertEquals(expectedCurrentHealthAfterHeal, endingHealth.getCurrentHitPoints());
    }

    @Test
    void testHeal_healingMoreThanMaxHP_doesNotExceedMaxHP() {
        final int startingCurrentHealth = 5;
        final int healAmount = 1000; 
        final Health startingHealth = new Health(BRIV_NAME, BRIV_MAX_HEALTH, startingCurrentHealth);

        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(startingHealth));

        final HealRequest request = new HealRequest(BRIV_NAME, healAmount);

        final Health endingHealth = this.hitPointsController.heal(request);

        assertEquals(BRIV_MAX_HEALTH, endingHealth.getCurrentHitPoints());
    }

    @Test
    void testHeal_creatureNotExist_throws() {
        final HealRequest request = new HealRequest(CREATURE_NOT_EXIST_NAME, 10);

        assertThrows(CreatureNotFoundException.class, 
            () -> this.hitPointsController.heal(request));
    }

    @Test
    void testHeal_nullRequest_throws() {
        assertThrows(NullPointerException.class, 
            () -> this.hitPointsController.heal(null));
    }

    @Test
    void testHeal_nullCreatureName_throws() {
        final HealRequest request = new HealRequest(null, 10);

        assertThrows(NullPointerException.class, 
            () -> this.hitPointsController.heal(request));
    }

    @Test
    void testHeal_negativeHealAmount_throws() {
        final HealRequest request = new HealRequest(BRIV_NAME, -10);

        assertThrows(IllegalArgumentException.class, 
            () -> this.hitPointsController.heal(request));
    }
}
