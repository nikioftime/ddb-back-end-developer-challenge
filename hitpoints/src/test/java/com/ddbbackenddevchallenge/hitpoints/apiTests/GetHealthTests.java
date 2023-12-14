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
import com.ddbbackenddevchallenge.hitpoints.model.data.Health;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetHealthTests {

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
    void testGetHealth_returnsHealth() {
        final Health output = this.hitPointsController.getHealth(BRIV_NAME);

        assertEquals(BRIV_STARTING_HEALTH, output);
    }

    @Test
    void testGetHealth_creatureNotExist_throws() {
        assertThrows(CreatureNotFoundException.class, 
            () -> this.hitPointsController.getHealth(CREATURE_NOT_EXIST_NAME));
    }

    @Test
    void testGetHealth_creatureNullName_throws() {
        assertThrows(NullPointerException.class, 
            () -> this.hitPointsController.getHealth(null));
    }
}
