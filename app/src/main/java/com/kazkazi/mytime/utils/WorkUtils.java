package com.kazkazi.mytime.utils;

import com.kazkazi.mytime.entities.Pause;
import com.kazkazi.mytime.entities.State;
import com.kazkazi.mytime.entities.Work;

import java.time.Duration;
import java.time.LocalDateTime;

public class WorkUtils {
    public static Duration getTotalPause(Work work) {
        Duration totalPause = Duration.ofNanos(0);
        for ( Pause p : work.getPauses()) {
            LocalDateTime stopped = LocalDateTime.now();
            if (p.getStoppedAt() != null) {
                stopped = p.getStoppedAt();
            }
            totalPause = totalPause.plus(Duration.between(p.getStartedAt(), stopped));
        }
        return totalPause;
    }

    public static Duration getDurationWorked(Work work) {
        Duration durationTimeWorked;
        if (work.getState() == State.STOPPED) {
            durationTimeWorked = Duration.between(work.getStartedFrom(), work.getEndedAt());
        } else {
            durationTimeWorked = Duration.between(work.getStartedFrom(), LocalDateTime.now());
        }
        durationTimeWorked = durationTimeWorked.minus(getTotalPause(work));
        return durationTimeWorked;
    }
}
