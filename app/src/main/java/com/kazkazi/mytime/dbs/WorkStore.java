package com.kazkazi.mytime.dbs;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {WorkStoreEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class WorkStore extends RoomDatabase{
    public abstract WorkStoreDao workStoreDao();

    private static WorkStore INSTANCE;


    public static WorkStore getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WorkStore.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WorkStore.class, "WorkStore")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

}
