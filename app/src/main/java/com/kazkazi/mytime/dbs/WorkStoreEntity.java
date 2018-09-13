package com.kazkazi.mytime.dbs;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.kazkazi.mytime.entities.Work;
import com.kazkazi.mytime.utils.TimeUtils;

import java.util.Date;
import java.util.UUID;

@Entity
public class WorkStoreEntity {
    @PrimaryKey
    @NonNull
    private UUID id;
    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name="data")
    private String data;

    public WorkStoreEntity() {
        id = UUID.randomUUID();
    }

    public WorkStoreEntity(Work work) {
        this();
        if (work.getId()!= null) {
            this.id = work.getId();
        }
        this.date = TimeUtils.convertLocalDateTimeToDate(work.getStartedFrom());
        Gson gson = new Gson();
        this.data = gson.toJson(work);
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
