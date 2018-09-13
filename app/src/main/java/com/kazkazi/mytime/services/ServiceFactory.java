package com.kazkazi.mytime.services;

import android.app.Application;

public class ServiceFactory {

    private static ServiceFactory instance;

    private final SettingsService settingsService;
    private final TimerService timerService;

    public synchronized  static ServiceFactory setup(Application application) {
        if (instance == null) {
            instance = new ServiceFactory(application);
        }
        return instance;
    }

    public synchronized static ServiceFactory getInstance() {
        if (instance == null) {
            throw new RuntimeException("call ServiceFactory.setup first before calling getInstance()!");
        }
        return instance;
    }

    private ServiceFactory(Application application) {
        this.timerService = new TimerService(application);
        this.settingsService = new SettingsService();
    }

    public SettingsService getSettingsService() {
        return this.settingsService;
    }

    public TimerService getTimerService() {
        return this.timerService;
    }
}
