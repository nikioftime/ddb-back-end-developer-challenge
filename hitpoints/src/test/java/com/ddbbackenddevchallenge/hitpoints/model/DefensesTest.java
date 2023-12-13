package com.ddbbackenddevchallenge.hitpoints.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ddbbackenddevchallenge.hitpoints.model.data.DamageType;
import com.ddbbackenddevchallenge.hitpoints.model.data.DefenseType;
import com.ddbbackenddevchallenge.hitpoints.model.data.Defenses;
import com.ddbbackenddevchallenge.hitpoints.model.data.Player.Defense;

public class DefensesTest {
    private static final String TEST_NAME = "test";
    private static final List<Defense> TEST_DEFENSE_LIST = List.of(
        new Defense(DamageType.FIRE, DefenseType.IMMUNITY),
        new Defense(DamageType.BLUDGEONING, DefenseType.IMMUNITY), 
        new Defense(DamageType.ACID, DefenseType.RESISTANCE),
        new Defense(DamageType.FIRE, DefenseType.RESISTANCE));

    @Test
    void testOnlySettingNameInConstructor() {
        final Defenses defenses = new Defenses(TEST_NAME);

        assertEquals(TEST_NAME, defenses.getName());
        assert(defenses.getImmunities().isEmpty());
        assert(defenses.getResistances().isEmpty());
    }

    @Test
    void testSettingNameAndDefensesInConstructor() {
        final Defenses defenses = new Defenses(TEST_NAME, TEST_DEFENSE_LIST);
        final List<DamageType> resistances = defenses.getResistances();
        final List<DamageType> immunities = defenses.getImmunities();

        assertEquals(TEST_NAME, defenses.getName());
        assert(resistances.contains(DamageType.FIRE));
        assert(resistances.contains(DamageType.ACID));
        assert(immunities.contains(DamageType.FIRE));
        assert(immunities.contains(DamageType.BLUDGEONING));
    }

    @Test
    void testGetDefenseForDamageTypeNoDefenseReturnsNone() {
        final Defenses defenses = new Defenses(TEST_NAME, TEST_DEFENSE_LIST);

        assertEquals(DefenseType.NONE, defenses.getDefenseForDamageType(DamageType.COLD));
    }

    @Test
    void testGetDefenseForDamageTypeResistanceReturnsResistance() {
        final Defenses defenses = new Defenses(TEST_NAME, TEST_DEFENSE_LIST);

        assertEquals(DefenseType.RESISTANCE, defenses.getDefenseForDamageType(DamageType.ACID));
    }

    @Test
    void testGetDefenseForDamageTypeImmunityReturnsImmunity() {
        final Defenses defenses = new Defenses(TEST_NAME, TEST_DEFENSE_LIST);

        assertEquals(DefenseType.IMMUNITY, defenses.getDefenseForDamageType(DamageType.BLUDGEONING));
    }

    @Test
    void testGetDefenseForDamageTypeResistanceAndImmunityReturnsImmunity() {
        final Defenses defenses = new Defenses(TEST_NAME, TEST_DEFENSE_LIST);

        assertEquals(DefenseType.IMMUNITY, defenses.getDefenseForDamageType(DamageType.FIRE));
    }

}