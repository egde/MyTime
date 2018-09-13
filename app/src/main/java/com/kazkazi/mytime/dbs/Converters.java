package com.kazkazi.mytime.dbs;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

   @TypeConverter
    public static UUID stringToUUID(String uuidStr) {
        return uuidStr == null ? null: UUID.fromString(uuidStr);
   }

    @TypeConverter
    public static String fromUUID(UUID uuid) {
        return uuid == null ? null: uuid.toString();
    }
}
