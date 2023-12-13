package com.ddbbackenddevchallenge.hitpoints.model.api.request;

public class HealRequest {
    private String name;
    private int healAmount;
    
    public HealRequest(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealAmount() {
        return healAmount;
    }
    
    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }
}
