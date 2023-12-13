package com.ddbbackenddevchallenge.hitpoints.model.api.request;

import com.ddbbackenddevchallenge.hitpoints.model.data.DamageType;

public class DamageRequest {
    private String name;
    private int damageAmount;
    private DamageType damageType;

    public DamageRequest(String name, int damageAmount, DamageType damageType) {
        this.name = name;
        this.damageAmount = damageAmount;
        this.damageType = damageType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(int damageAmount) {
        this.damageAmount = damageAmount;
    }

    public DamageType getDamageType() {
        return damageType;
    }
    
    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }
}
