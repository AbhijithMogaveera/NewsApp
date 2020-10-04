package com.abhijith.newsapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhijith.newsapp.models.Source

@Dao
interface SourcesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun bulkInsert(sources: List<Source>)

    @get:Query("SELECT * FROM sources")
    val allSources: LiveData<List<Source>>

}