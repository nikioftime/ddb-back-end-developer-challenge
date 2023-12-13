package com.ddbbackenddevchallenge.hitpoints.model.data;

import com.google.gson.annotations.SerializedName;

public enum DefenseType {
    @SerializedName("resistance")
    RESISTANCE,
    @SerializedName("immunity")
    IMMUNITY,
    @SerializedName("none")
    NONE
}
