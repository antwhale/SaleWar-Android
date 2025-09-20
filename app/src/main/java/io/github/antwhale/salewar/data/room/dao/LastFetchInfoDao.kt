package io.github.antwhale.salewar.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.antwhale.salewar.data.room.entity.LastFetchInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface LastFetchInfoDao {
    @Query("DELETE FROM LastFetchInfo")
    suspend fun deleteAll()

    @Insert
    suspend fun insertLastFetchInfo(info: LastFetchInfo)

    @Query("SELECT * FROM LastFetchInfo")
    suspend fun getAll(): List<LastFetchInfo>
}