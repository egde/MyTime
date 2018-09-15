package com.kazkazi.mytime.services;

import android.app.Application;

import com.kazkazi.mytime.dbs.WorkEntityRepo;
import com.kazkazi.mytime.dbs.WorkStoreEntity;
import com.kazkazi.mytime.entities.Pause;
import com.kazkazi.mytime.entities.State;
import com.kazkazi.mytime.entities.Work;

import java.time.LocalDateTime;

public class TimerService {

    private Work work;
    private final WorkEntityRepo repo;

    public TimerService(Application app) {
        repo = new WorkEntityRepo(app);
        repo.getLatestWork((Work work) -> this.work = work);
    }

    public void start() {
        this.work = new Work();
        this.work.setStartedFrom(LocalDateTime.now());
        this.work.setState(State.IN_PROGRESS);
        repo.insert(work, (WorkStoreEntity response) -> work.setId(response.getId()));
    }

    public void stop() {
        if (this.work.getState() != State.STOPPED) {
            this.work.setEndedAt(LocalDateTime.now());
            this.work.setState(State.STOPPED);
            repo.update(work);
        }
    }

    public void pause() {
        if (this.work.getState() == State.IN_PROGRESS) {
            Pause pause = new Pause();
            pause.setStartedAt(LocalDateTime.now());
            this.work.getPauses().add(pause);

            this.work.setState(State.PAUSED);
            repo.update(work);
        }
    }

    public void unpause() {
        if (this.work.getState() == State.PAUSED) {
            this.work.getLastPause().setStoppedAt(LocalDateTime.now());
            this.work.setState(State.IN_PROGRESS);
            repo.update(work);
        }
    }

    public Work getWork() {
        return this.work;
    }

    public void getAllWork() {
    }
}
