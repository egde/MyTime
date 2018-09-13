package com.kazkazi.mytime;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kazkazi.mytime.entities.Pause;
import com.kazkazi.mytime.entities.State;
import com.kazkazi.mytime.services.ServiceFactory;
import com.kazkazi.mytime.services.SettingsService;
import com.kazkazi.mytime.services.TimerService;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private TimerService timerService;
    private SettingsService settingsService;

    @BindView(R.id.btnStart) Button btnStart;
    @BindView(R.id.btnStop) Button btnStop;
    @BindView(R.id.btnPause) Button btnPause;
    @BindView(R.id.tvStarted) TextView tvStarted;
    @BindView(R.id.tvTimeToWork) TextView tvTimeToWork;
    @BindView(R.id.tvTimeWorked) TextView tvTimeWorked;

    //runs without a timer by reposting this handler at the end of the runnable
    private final Handler timerHandler = new Handler();
    private final Runnable timerRunnable;

    {
        timerRunnable = new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                if (timerService.getWork() == null || timerService.getWork().getStartedFrom() == null) {
                    tvStarted.setText("-");
                    tvTimeToWork.setText("--:--");
                } else {

                    Duration totalPause = Duration.ofNanos(0);
                    for ( Pause p : timerService.getWork().getPauses()) {
                        LocalDateTime stopped = LocalDateTime.now();
                        if (p.getStoppedAt() != null) {
                            stopped = p.getStoppedAt();
                        }
                        totalPause = totalPause.plus(Duration.between(p.getStartedAt(), stopped));
                    }

                    // tvStarted
                    DateTimeFormatter frmtStarted = DateTimeFormatter.ofPattern("EEE d.MM.yyyy HH:mm");
                    tvStarted.setText(timerService.getWork().getStartedFrom().format(frmtStarted));


                    // tvTimeToWork
                    if (timerService.getWork().getState() == State.STOPPED) {
                        tvTimeToWork.setText("--:--");
                    } else {
                        LocalDateTime timeToWork = timerService.getWork().getStartedFrom().plusMinutes(settingsService.getMinsToWork()).plus(totalPause);
                        DateTimeFormatter frmtTimeToWork = DateTimeFormatter.ofPattern("HH:mm");
                        tvTimeToWork.setText(timeToWork.format(frmtTimeToWork));
                    }

                    // tvTimeWorked
                    Duration durationTimeWorked;
                    if (timerService.getWork().getState() == State.STOPPED) {
                        durationTimeWorked = Duration.between(timerService.getWork().getStartedFrom(), timerService.getWork().getEndedAt());
                    } else {
                        durationTimeWorked = Duration.between(timerService.getWork().getStartedFrom(), LocalDateTime.now());
                    }
                    durationTimeWorked = durationTimeWorked.minus(totalPause);
                    tvTimeWorked.setText(DurationFormatUtils.formatDuration(durationTimeWorked.toMillis(), "HH:mm:ss"));
                    updateButtons();

                    if (timerService.getWork().getState() != State.NOT_STARTED && timerService.getWork().getState() != State.STOPPED) {
                        timerHandler.postDelayed(this, 500);
                    }
                }


            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Show all times", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);

        ServiceFactory sf = ServiceFactory.setup(this.getApplication());
        timerService = sf.getTimerService();
        settingsService = sf.getSettingsService();


        updateButtons();
    }

    private void updateButtons() {
        if (this.timerService.getWork() == null) {
            btnStart.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.GONE);
            btnPause.setVisibility(View.GONE);
        } else {

            switch (this.timerService.getWork().getState()) {
                case NOT_STARTED:
                    btnStart.setVisibility(View.VISIBLE);
                    btnStop.setVisibility(View.GONE);
                    btnPause.setVisibility(View.GONE);
                    break;
                case IN_PROGRESS:
                    btnStart.setVisibility(View.GONE);
                    btnStop.setVisibility(View.VISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                    break;
                case PAUSED:
                    btnStart.setVisibility(View.VISIBLE);
                    btnStop.setVisibility(View.VISIBLE);
                    btnPause.setVisibility(View.GONE);
                    break;
                case STOPPED:
                    btnStart.setVisibility(View.VISIBLE);
                    btnStop.setVisibility(View.GONE);
                    btnPause.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.timerHandler.removeCallbacks(this.timerRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.timerHandler.postDelayed(this.timerRunnable,250);
    }


    public void startWork(View view) {
        if (
                this.timerService.getWork() == null ||
                this.timerService.getWork().getState() == State.NOT_STARTED ||
                this.timerService.getWork().getState() == State.STOPPED) {
            this.timerService.start();
        }
        if (this.timerService.getWork().getState() == State.PAUSED) {
            this.timerService.unpause();
        }
        updateButtons();
        this.timerHandler.postDelayed(this.timerRunnable,250);
    }

    public void stopWork(@SuppressWarnings("unused") View view) {
        this.timerService.stop();
        updateButtons();
        this.timerHandler.removeCallbacks(this.timerRunnable);
    }

    public void pauseWork(View view) {
        this.timerService.pause();
        updateButtons();
    }
}
