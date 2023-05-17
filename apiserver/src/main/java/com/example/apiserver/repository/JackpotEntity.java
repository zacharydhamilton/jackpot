package com.example.apiserver.repository;

import com.example.apiserver.objects.Jackpot;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class JackpotEntity {
    @Id
    private String jackpotPoolId;
    private String region;
    private int jackpot;

    public JackpotEntity() {}
    public JackpotEntity(Jackpot jackpot) {
        this.jackpotPoolId = (String) jackpot.getJackpotPoolId();
        this.region = (String) jackpot.getRegion();
        this.jackpot = jackpot.getJackpot();
    }

    public String getJackpotPoolId() {
        return this.jackpotPoolId;
    }
    public void setJackpotPoolId(String jackpotPoolId) {
        this.jackpotPoolId = jackpotPoolId;
    }
    public String getRegion() {
        return this.region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public int getJackpot() {
        return this.jackpot;
    }
    public void setJackpot(int jackpot) {
        this.jackpot = jackpot;
    }

}
