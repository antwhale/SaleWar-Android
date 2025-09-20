package io.github.antwhale.salewar.data.room

import android.content.Context
import android.util.Log
import androidx.room.Room
import io.github.antwhale.salewar.data.room.dao.FavoriteProductDao
import io.github.antwhale.salewar.data.room.dao.LastFetchInfoDao
import io.github.antwhale.salewar.data.room.dao.ProductDao
import io.github.antwhale.salewar.data.room.entity.LastFetchInfo
import kotlinx.coroutines.flow.Flow

object RoomManager {
    val TAG = "RoomManager"
    lateinit var db : AppDatabase
    lateinit var productDao: ProductDao
    lateinit var lastFetchInfoDao: LastFetchInfoDao
    lateinit var favoriteProductDao: FavoriteProductDao

    fun initialize(context: Context) {
        Log.d(TAG, "initialize")
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        ).build()
        productDao = db.productDao()
        lastFetchInfoDao = db.lastFetchInfoDao()
        favoriteProductDao = db.favoriteProductDao()
    }

    suspend fun getLastFetchDate() : List<LastFetchInfo> = lastFetchInfoDao.getAll()

    fun release() {
        Log.d(TAG, "release: ")
        db.close()
    }
}