package com.kazkazi.mytime.entities;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Work {
    private UUID id;
    private LocalDateTime startedFrom;
    private LocalDateTime endedAt;
    private final List<Pause> pauses = new ArrayList<>();
    private State state = State.NOT_STARTED;

    public LocalDateTime getStartedFrom() {
        return startedFrom;
    }

    public void setStartedFrom(LocalDateTime startedFrom) {
        this.startedFrom = startedFrom;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public List<Pause> getPauses() {
        return pauses;
    }

    public Pause getLastPause() {
        if (this.pauses.isEmpty()) {
            return null;
        }
        return this.pauses.get(this.pauses.size()-1);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public static Work fromJson(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, Work.class);
    }
}
