package com.ddbbackenddevchallenge.hitpoints.model.data;

import java.util.List;

/**
 * Model for reading in character sheet JSON file
 */
public class Player {
    private String name;
    private int hitPoints;
    private List<Defense> defenses;

    public Player(String name, int hitPoints, List<Defense> defenses) {
        this.name = name;
        this.hitPoints = hitPoints;
        this.defenses = defenses;
    }

    public static class Defense {
        private DamageType type;
        private DefenseType defense;
    
        public Defense(DamageType type, DefenseType defense) {
            this.type = type;
            this.defense = defense;
        }
        @Override
        public String toString() {
            return "Defense [type=" + type + ", defense=" + defense + "]";
        }

        public DamageType getType() {
            return type;
        }
        public DefenseType getDefense() {
            return defense;
        }
    }

    @Override
    public String toString() {
        return "Character [name=" + name + ", hitPoints=" + hitPoints + ", defenses=" + defenses + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public List<Defense> getDefenses() {
        return defenses;
    }

    public void setDefenses(List<Defense> defenses) {
        this.defenses = defenses;
    }
    
}
