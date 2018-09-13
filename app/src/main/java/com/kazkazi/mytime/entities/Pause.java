package com.kazkazi.mytime.entities;

import java.time.LocalDateTime;

public class Pause {

    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(LocalDateTime stoppedAt) {
        this.stoppedAt = stoppedAt;
    }
}
