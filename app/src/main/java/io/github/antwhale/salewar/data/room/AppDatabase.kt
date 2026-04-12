package io.github.antwhale.salewar.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.antwhale.salewar.data.room.dao.FavoriteProductDao
import io.github.antwhale.salewar.data.room.dao.LastFetchInfoDao
import io.github.antwhale.salewar.data.room.dao.ProductDao

import io.github.antwhale.salewar.data.room.entity.FavoriteProduct
import io.github.antwhale.salewar.data.room.entity.LastFetchInfo
import io.github.antwhale.salewar.data.room.entity.Product

@Database(entities = [Product::class, LastFetchInfo::class, FavoriteProduct::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun productDao(): ProductDao

    abstract fun lastFetchInfoDao(): LastFetchInfoDao

    abstract fun favoriteProductDao(): FavoriteProductDao

    companion object {
        // 1에서 2로 가는 마이그레이션 로직 정의
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // ALTER TABLE 명령어로 컬럼 추가
                database.execSQL("ALTER TABLE Product ADD COLUMN category TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE Product ADD COLUMN description TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE FavoriteProduct ADD COLUMN category TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE FavoriteProduct ADD COLUMN description TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}