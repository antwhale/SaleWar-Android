package io.github.antwhale.salewar.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.antwhale.salewar.data.room.dao.FavoriteProductDao
import io.github.antwhale.salewar.data.room.dao.LastFetchInfoDao
import io.github.antwhale.salewar.data.room.dao.ProductDao

import io.github.antwhale.salewar.data.room.entity.FavoriteProduct
import io.github.antwhale.salewar.data.room.entity.LastFetchInfo
import io.github.antwhale.salewar.data.room.entity.Product

@Database(entities = [Product::class, LastFetchInfo::class, FavoriteProduct::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun productDao(): ProductDao

    abstract fun lastFetchInfoDao(): LastFetchInfoDao

    abstract fun favoriteProductDao(): FavoriteProductDao
}