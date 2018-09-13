package com.kazkazi.mytime.services;

public class SettingsService {
    private int minsToWork = 8*60;

    public int getMinsToWork() {
        return minsToWork;
    }

    public void setMinsToWork(int minsToWork) {
        this.minsToWork = minsToWork;
    }
}
