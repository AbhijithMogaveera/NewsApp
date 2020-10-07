package com.abhijith.newsapp.room.databaseconverters

import androidx.room.TypeConverter
import java.sql.Timestamp

class DatabaseConverters {

    @TypeConverter
    fun toJavaTimestamp(timestamp: Long?): Timestamp? {
        return if (timestamp == null) null else Timestamp(timestamp)
    }

    @TypeConverter
    fun toDatabaseTimestamp(timestamp: Timestamp?): Long {
        return timestamp?.time ?: Timestamp(System.currentTimeMillis()).time
    }

}