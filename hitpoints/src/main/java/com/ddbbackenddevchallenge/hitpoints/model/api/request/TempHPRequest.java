package com.ddbbackenddevchallenge.hitpoints.model.api.request;

public class TempHPRequest {
    private String name;
    private int tempHPAmount;

    public TempHPRequest(String name, int tempHPAmount) {
        this.name = name;
        this.tempHPAmount = tempHPAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTempHPAmount() {
        return tempHPAmount;
    }
    
    public void setTempHPAmount(int tempHPAmount) {
        this.tempHPAmount = tempHPAmount;
    }
}
