package com.kazkazi.mytime.dbs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface WorkStoreDao {

    @Query("SELECT id, date, data FROM WorkStoreEntity")
    List<WorkStoreEntity> getAll();

    @Query("Select id, date, data from WorkStoreEntity, (SELECT max(date) as maxdate from WorkStoreEntity) as a where date = a.maxdate")
    WorkStoreEntity getLastWorkStoreEntity();

    @Insert
    void insert(WorkStoreEntity workStoreEntity);

    @Update
    void update(WorkStoreEntity workStoreEntity);

    @Delete
    void delete(WorkStoreEntity workStoreEntity);

}
