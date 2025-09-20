package io.github.antwhale.salewar.data.room.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FavoriteProductDao {
    @Query("DELETE FROM FavoriteProduct")
    suspend fun deleteAll()
}