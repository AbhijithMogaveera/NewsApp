package com.abhijith.newsapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.abhijith.newsapp.models.Source;

import java.util.List;

@Dao
public interface SourcesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void bulkInsert(List<Source> sources);

    @Query("SELECT * FROM sources")
    LiveData<List<Source>> getAllSources();
}
