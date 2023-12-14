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
import com.ddbbackenddevchallenge.hitpoints.model.api.request.DamageRequest;
import com.ddbbackenddevchallenge.hitpoints.model.data.DamageType;
import com.ddbbackenddevchallenge.hitpoints.model.data.DefenseType;
import com.ddbbackenddevchallenge.hitpoints.model.data.Defenses;
import com.ddbbackenddevchallenge.hitpoints.model.data.Health;
import com.ddbbackenddevchallenge.hitpoints.model.data.Player.Defense;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DamageTests {
        private static final String BRIV_NAME = "Briv";
    private static final String CREATURE_NOT_EXIST_NAME = "Reeza";
    private static final int BRIV_MAX_HEALTH = 25;
    private static final DamageType BRIV_DEFAULT_IMMUNITY = DamageType.FIRE;
    private static final DamageType BRIV_DEFAULT_RESISTANCE = DamageType.SLASHING;
    private static final Defenses BRIV_DEFAULT_DEFENSES = new Defenses(BRIV_NAME, 
        List.of(
            new Defense(BRIV_DEFAULT_IMMUNITY, DefenseType.IMMUNITY),
            new Defense(BRIV_DEFAULT_RESISTANCE, DefenseType.RESISTANCE)
        ));

    @Mock
    private HealthRepository healthRepository;
    @Mock
    private DefensesRepository defensesRepository;

    private HitPointsController hitPointsController;

    @BeforeEach
    void setup() {
        when(defensesRepository.findById(BRIV_NAME)).thenReturn(Optional.of(BRIV_DEFAULT_DEFENSES));
        
        this.hitPointsController = new HitPointsController(this.healthRepository, this.defensesRepository);
    }

    @Test
    void testDamage_noDefensesForDamageType_DamageApplies() {
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(new Health(BRIV_NAME, BRIV_MAX_HEALTH)));
        
        final int damageAmount = 10;
        final DamageRequest request = new DamageRequest(BRIV_NAME, damageAmount, DamageType.COLD);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        final int expectedCurrentHealthAfterDamage = BRIV_MAX_HEALTH - damageAmount;

        assertEquals(expectedCurrentHealthAfterDamage, healthAfterDamage.getCurrentHitPoints());
    }

    @Test
    void testDamage_noDefensesForDamageType_tempHP_DamageAppliesToTempHPFirst() {
        final int initialTempHP = 20;
        final Health startingHealth = new Health(BRIV_NAME, BRIV_MAX_HEALTH, BRIV_MAX_HEALTH, initialTempHP);
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(startingHealth));

        final int damageAmount = 10;

        final DamageRequest request = new DamageRequest(BRIV_NAME, damageAmount, DamageType.COLD);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        final int expectedTempHPAfterDamage = initialTempHP - damageAmount;

        assertEquals(expectedTempHPAfterDamage, healthAfterDamage.getTemporaryHitPoints());
        assertEquals(BRIV_MAX_HEALTH, healthAfterDamage.getCurrentHitPoints());
    }

    @Test
    void testDamage_noDefensesForDamageType_tempHPExceedsDamageTaken_DamageAppliesToTempHPFirst() {
        final int initialTempHP = 5;
        final Health startingHealth = new Health(BRIV_NAME, BRIV_MAX_HEALTH, BRIV_MAX_HEALTH, initialTempHP);
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(startingHealth));

        final int damageAmount = 10;

        final DamageRequest request = new DamageRequest(BRIV_NAME, damageAmount, DamageType.COLD);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        final int expectedCurrentHPAfterDamage = BRIV_MAX_HEALTH - (damageAmount - initialTempHP);

        assertEquals(0, healthAfterDamage.getTemporaryHitPoints());
        assertEquals(expectedCurrentHPAfterDamage, healthAfterDamage.getCurrentHitPoints());
    }

    @Test
    void testDamage_immuneToDamage_noDamageTaken() {
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(new Health(BRIV_NAME, BRIV_MAX_HEALTH)));

        final DamageRequest request = new DamageRequest(BRIV_NAME, 10, BRIV_DEFAULT_IMMUNITY);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        assertEquals(BRIV_MAX_HEALTH, healthAfterDamage.getCurrentHitPoints());
    }

    @Test
    void testDamage_immuneAndResistantToDamage_noDamageTaken() {
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(new Health(BRIV_NAME, BRIV_MAX_HEALTH)));

        final Defenses defenses = new Defenses(BRIV_NAME, 
            List.of(
                new Defense(BRIV_DEFAULT_IMMUNITY, DefenseType.IMMUNITY),
                new Defense(BRIV_DEFAULT_IMMUNITY, DefenseType.RESISTANCE)
            ));

        when(defensesRepository.findById(BRIV_NAME)).thenReturn(Optional.of(defenses));

        final DamageRequest request = new DamageRequest(BRIV_NAME, 10, BRIV_DEFAULT_IMMUNITY);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        assertEquals(BRIV_MAX_HEALTH, healthAfterDamage.getCurrentHitPoints());
    }

    @Test
    void testDamage_resistanceToDamage_DamageHalved() {
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(new Health(BRIV_NAME, BRIV_MAX_HEALTH)));

        final int damageAmount = 10;
        final DamageRequest request = new DamageRequest(BRIV_NAME, damageAmount, BRIV_DEFAULT_RESISTANCE);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        final int expectedCurrentHealthAfterDamage = BRIV_MAX_HEALTH - (damageAmount / 2);

        assertEquals(expectedCurrentHealthAfterDamage, healthAfterDamage.getCurrentHitPoints());
    }

    @Test
    void testDamage_resistanceToDamage_oddDamageNumber_damageHalvedAndRoundedDown() {
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(new Health(BRIV_NAME, BRIV_MAX_HEALTH)));

        final int damageAmount = 15;
        final DamageRequest request = new DamageRequest(BRIV_NAME, damageAmount, BRIV_DEFAULT_RESISTANCE);

        final Health healthBefore = this.hitPointsController.getHealth(BRIV_NAME);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        final int expectedCurrentHealthAfterDamage = BRIV_MAX_HEALTH - (damageAmount / 2);

        assertEquals(expectedCurrentHealthAfterDamage, healthAfterDamage.getCurrentHitPoints(), healthBefore.toString() + healthAfterDamage.toString());
    }

    @Test
    void testDamage_resistanceToDamage_tempHP_DamageHalved() {
         final int initialTempHP = 20;
        final Health startingHealth = new Health(BRIV_NAME, BRIV_MAX_HEALTH, BRIV_MAX_HEALTH, initialTempHP);
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(startingHealth));

        final int damageAmount = 10;

        final DamageRequest request = new DamageRequest(BRIV_NAME, damageAmount, BRIV_DEFAULT_RESISTANCE);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        final int expectedTempHPAfterDamage = initialTempHP - (damageAmount / 2);

        assertEquals(expectedTempHPAfterDamage, healthAfterDamage.getTemporaryHitPoints());
        assertEquals(BRIV_MAX_HEALTH, healthAfterDamage.getCurrentHitPoints());
    }

    @Test
    void testDamage_damageExceedsMaxHP_HPDropsToZero() {
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(new Health(BRIV_NAME, BRIV_MAX_HEALTH)));

        final int damageAmount = BRIV_MAX_HEALTH * 2;
        final DamageRequest request = new DamageRequest(BRIV_NAME, damageAmount, DamageType.COLD);

        final Health healthAfterDamage = this.hitPointsController.damage(request);

        assertEquals(0, healthAfterDamage.getCurrentHitPoints());
    }

    @Test
    void testDamage_creatureNotExist_throws() {
        when(healthRepository.findById(CREATURE_NOT_EXIST_NAME)).thenReturn(Optional.empty());

        final DamageRequest request = new DamageRequest(CREATURE_NOT_EXIST_NAME, 10, DamageType.COLD);

        assertThrows(CreatureNotFoundException.class, 
            () -> this.hitPointsController.damage(request));
    }

    @Test
    void testDamage_nullRequest_throws() {
        assertThrows(NullPointerException.class, 
            () -> this.hitPointsController.damage(null));
    }

    @Test
    void testDamage_nullCreatureName_throws() {
        final DamageRequest request = new DamageRequest(null, 10, DamageType.COLD);

        assertThrows(NullPointerException.class, 
            () -> this.hitPointsController.damage(request));
    }

    @Test
    void testDamage_negativeDamageAmount_throws() {
        final DamageRequest request = new DamageRequest(BRIV_NAME, -10, DamageType.COLD);

        assertThrows(IllegalArgumentException.class, 
            () -> this.hitPointsController.damage(request));
    }

    @Test
    void testDamage_nullDamageType_throws() {
        final DamageRequest request = new DamageRequest(BRIV_NAME, -10, null);

        assertThrows(NullPointerException.class, 
            () -> this.hitPointsController.damage(request));
    }
}
