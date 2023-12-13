package com.ddbbackenddevchallenge.hitpoints.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ddbbackenddevchallenge.hitpoints.model.data.Health;

public class HealthTest {
    private static final String TEST_NAME = "test";
    private static final int TEST_MAX_HEALTH = 13;
    private static final int TEST_CURRENT_HEALTH = 8;
    private static final int TEST_TEMPORARY_HEALTH = 2;

    @Test
    void testOnlySettingMaxHPInConstructor() {
        final Health testHealth = new Health("test", TEST_MAX_HEALTH);

        assertEquals(TEST_MAX_HEALTH, testHealth.getMaxHitPoints());
        assertEquals(TEST_MAX_HEALTH, testHealth.getCurrentHitPoints());
        assertEquals(0, testHealth.getTemporaryHitPoints());
    }

    @Test
    void testSettingMaxAndCurrentHPInConstructor() {
        final Health testHealth = new Health("test", TEST_MAX_HEALTH, TEST_CURRENT_HEALTH);

        assertEquals(TEST_MAX_HEALTH, testHealth.getMaxHitPoints());
        assertEquals(TEST_CURRENT_HEALTH, testHealth.getCurrentHitPoints());
        assertEquals(0, testHealth.getTemporaryHitPoints());
    }

    @Test
    void testSettingAllFieldsInConstructor() {
        final Health testHealth = new Health("test", TEST_MAX_HEALTH, TEST_CURRENT_HEALTH, TEST_TEMPORARY_HEALTH);

        assertEquals(TEST_MAX_HEALTH, testHealth.getMaxHitPoints());
        assertEquals(TEST_CURRENT_HEALTH, testHealth.getCurrentHitPoints());
        assertEquals(TEST_TEMPORARY_HEALTH, testHealth.getTemporaryHitPoints());
    }

    @Test
    void testSettingNegativeHealthThrows() {
        // negative max health
        assertThrows(IllegalArgumentException.class, () -> new Health(TEST_NAME, -1, TEST_CURRENT_HEALTH, 
            TEST_TEMPORARY_HEALTH));

        // negative max health
        assertThrows(IllegalArgumentException.class, () -> new Health(TEST_NAME, TEST_MAX_HEALTH, -1, 
            TEST_TEMPORARY_HEALTH));

        // negative max health
        assertThrows(IllegalArgumentException.class, () -> new Health(TEST_NAME, TEST_MAX_HEALTH, 
            TEST_CURRENT_HEALTH, -1));
    }

    @Test
    void testSettingLargerCurrentHPThanMaxHPThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Health(TEST_NAME, TEST_MAX_HEALTH, 
            TEST_MAX_HEALTH + 5));
    }

    @Test
    void testEquals() {
        final Health testHealth1 = new Health(TEST_NAME, TEST_MAX_HEALTH, TEST_CURRENT_HEALTH, TEST_TEMPORARY_HEALTH);
        final Health testHealth2 = new Health(TEST_NAME, TEST_MAX_HEALTH, TEST_CURRENT_HEALTH, TEST_TEMPORARY_HEALTH);

        assertEquals(testHealth1, testHealth2);
    }

    @Test
    void testSetCurrentHitPoints() {
        Health testHealth = getDefaultHealth();
        final int newCurrentHP = 5;

        testHealth.setCurrentHitPoints(newCurrentHP);
        assertEquals(newCurrentHP, testHealth.getCurrentHitPoints());
    }

    @Test
    void testSetCurrentHitPointsAsNegativeThrows() {
        Health testHealth = getDefaultHealth();

        assertThrows(IllegalArgumentException.class, () -> testHealth.setCurrentHitPoints(-1));
    }

    @Test
    void testSetCurrentHitPointsGreaterThanMaxHealthThrows() {
        Health testHealth = getDefaultHealth();

        assertThrows(IllegalArgumentException.class, () -> testHealth.setCurrentHitPoints(TEST_MAX_HEALTH * 2));
    }

    @Test
    void testSetMaxHitPoints() {
        Health testHealth = getDefaultHealth();
        final int newMaxHP = 5;

        testHealth.setMaxHitPoints(newMaxHP);
        assertEquals(newMaxHP, testHealth.getMaxHitPoints());
    }

    @Test
    void testSetMaxHitPointsAsNegativeThrows() {
        Health testHealth = getDefaultHealth();

        assertThrows(IllegalArgumentException.class, () -> testHealth.setMaxHitPoints(-1));
    }

    @Test
    void testSetTemporaryHitPoints() {
        Health testHealth = getDefaultHealth();
        final int newTemporaryHP = 5;

        testHealth.setTemporaryHitPoints(newTemporaryHP);
        assertEquals(newTemporaryHP, testHealth.getTemporaryHitPoints());
    }

    @Test
    void testSetTemporaryHitPointsAsNegativeThrows() {
        Health testHealth = getDefaultHealth();

        assertThrows(IllegalArgumentException.class, () -> testHealth.setTemporaryHitPoints(-1));
    }

    @Test
    void testToString() {

    }

    private Health getDefaultHealth() {
        return new Health(TEST_NAME, TEST_MAX_HEALTH, TEST_CURRENT_HEALTH, TEST_TEMPORARY_HEALTH);
    }
}
