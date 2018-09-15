package com.kazkazi.mytime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kazkazi.mytime.dbs.WorkEntityRepo;
import com.kazkazi.mytime.entities.Work;
import com.kazkazi.mytime.utils.WorkUtils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WorkTimesActivity extends AppCompatActivity {

    private WorkTimeAdapter workTimeAdapter;
    private WorkEntityRepo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_times);

        workTimeAdapter = new WorkTimeAdapter(this, new ArrayList<>());
        ListView lvWork = findViewById(R.id.lvWork);
        lvWork.setAdapter(workTimeAdapter);

        repo = new WorkEntityRepo(this.getApplication());
        repo.getAllWork((List<Work> result) -> workTimeAdapter.setWorkList(result));
    }

    private class WorkTimeAdapter extends BaseAdapter {

        private List<Work> workList;
        private Context context;

        public WorkTimeAdapter(Context context, List<Work> workList) {
            this.workList = workList;
            this.context = context;
        }

        public void setWorkList(List<Work> workList) {
            this.workList = workList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return workList.size();
        }

        @Override
        public Object getItem(int i) {
            return workList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            WorkTimeEntryViewHolder holder;

            if (view == null){
                LayoutInflater workTimeEntryInflater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                view = workTimeEntryInflater.inflate(R.layout.work_time_entry, null);

                holder = new WorkTimeEntryViewHolder();
                holder.txtDate = view.findViewById(R.id.txtDate);
                holder.txtStarted = view.findViewById(R.id.txtStarted);
                holder.txtEnd = view.findViewById(R.id.txtEnd);
                holder.txtHrsWorked = view.findViewById(R.id.txtHrsWorked);
                view.setTag(holder);
            } else {
                holder = (WorkTimeEntryViewHolder) view.getTag();
            }

            Work workItem = (Work) getItem(i);

            holder.txtDate.setText(workItem.getStartedFrom().format( DateTimeFormatter.ofPattern("d.MM.yyyy")));
            holder.txtStarted.setText(workItem.getStartedFrom().format( DateTimeFormatter.ofPattern("HH:mm")));
            holder.txtEnd.setText(workItem.getStartedFrom().format( DateTimeFormatter.ofPattern("HH:mm")));
            Duration durationTimeWorked = WorkUtils.getDurationWorked(workItem);
            holder.txtHrsWorked.setText(DurationFormatUtils.formatDuration(durationTimeWorked.toMillis(), "HH:mm:ss"));

            return view;
        }

        private class WorkTimeEntryViewHolder {
            public TextView txtDate;
            public TextView txtStarted;
            public TextView txtEnd;
            public TextView txtHrsWorked;
        }
    }
}
