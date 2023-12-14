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
import com.ddbbackenddevchallenge.hitpoints.model.api.request.TempHPRequest;
import com.ddbbackenddevchallenge.hitpoints.model.data.Health;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddTemporaryHPTests {
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
        
        this.hitPointsController = new HitPointsController(this.healthRepository, this.defensesRepository);
    }

    @Test
    void testAddTemporaryHP_noTemporaryHP_applies() {
        final int tempHPAmount = 10;
        final TempHPRequest request = new TempHPRequest(BRIV_NAME, tempHPAmount);

        final Health healthAfter = hitPointsController.addTemporaryHitPoints(request);

        assertEquals(tempHPAmount, healthAfter.getTemporaryHitPoints());
    }

    @Test
    void testAddTemporaryHP_existingLowerTemporaryHP_applies() {
        final int tempHPAmount = 10;
        final Health healthBefore = new Health(BRIV_NAME, BRIV_MAX_HEALTH, BRIV_MAX_HEALTH, 5);
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(healthBefore));

        final TempHPRequest request = new TempHPRequest(BRIV_NAME, tempHPAmount);

        final Health healthAfter = hitPointsController.addTemporaryHitPoints(request);

        assertEquals(tempHPAmount, healthAfter.getTemporaryHitPoints());
    }

    @Test
    void testAddTemporaryHP_existingTempHPHigherThanRequest_doesNotApply() {
        final int tempHPAmountBefore = 50;
        final int tempHPAmountRequest = 10;
        final Health healthBefore = new Health(BRIV_NAME, BRIV_MAX_HEALTH, BRIV_MAX_HEALTH, tempHPAmountBefore);
        when(healthRepository.findById(BRIV_NAME)).thenReturn(Optional.of(healthBefore));
        
        final TempHPRequest request = new TempHPRequest(BRIV_NAME, tempHPAmountRequest);

        final Health healthAfter = hitPointsController.addTemporaryHitPoints(request);

        assertEquals(tempHPAmountBefore, healthAfter.getTemporaryHitPoints());
    }

    @Test
    void testAddTemporaryHP_creatureNotExist_throws() {
        final TempHPRequest request = new TempHPRequest(CREATURE_NOT_EXIST_NAME, 10);

        assertThrows(CreatureNotFoundException.class, 
            () -> this.hitPointsController.addTemporaryHitPoints(request));
    }

    @Test
    void testAddTemporaryHP_nullRequest_throws() {
        assertThrows(NullPointerException.class, 
            () -> this.hitPointsController.addTemporaryHitPoints(null));
    }

    @Test
    void testAddTemporaryHP_nullCreatureName_throws() {
        final TempHPRequest request = new TempHPRequest(null, 10);

        assertThrows(NullPointerException.class, 
            () -> this.hitPointsController.addTemporaryHitPoints(request));
    }

    @Test
    void testAddTemporaryHP_negativeHealAmount_throws() {
        final TempHPRequest request = new TempHPRequest(BRIV_NAME, -10);

        assertThrows(IllegalArgumentException.class, 
            () -> this.hitPointsController.addTemporaryHitPoints(request));
    }
}
