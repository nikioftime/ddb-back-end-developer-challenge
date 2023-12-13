package com.ddbbackenddevchallenge.hitpoints.model.data;

import com.google.gson.annotations.SerializedName;

public enum DamageType {
    @SerializedName("bludgeoning")
    BLUDGEONING("bludgeoning"),
    @SerializedName("piercing")
    PIERCING("piercing"),
    @SerializedName("slashing")
    SLASHING("slashing"),
    @SerializedName("fire")
    FIRE("fire"),
    @SerializedName("cold")
    COLD("cold"),
    @SerializedName("acid")
    ACID("acid"),
    @SerializedName("thunder")
    THUNDER("thunder"),
    @SerializedName("lightning")
    LIGHTNING("lightning"),
    @SerializedName("poison")
    POISON("poison"),
    @SerializedName("radiant")
    RADIANT("radiant"),
    @SerializedName("necrotic")
    NECROTIC("necrotic"),
    @SerializedName("psychic")
    PSYCHIC("psychic"),
    @SerializedName("force")
    FORCE("force");

    private final String type;
    DamageType(final String type) { 
        this.type = type;
    }

    public String toString() {
        return type;
    }
}
