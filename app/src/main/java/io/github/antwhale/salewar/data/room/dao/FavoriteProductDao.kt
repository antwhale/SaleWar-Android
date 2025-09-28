package io.github.antwhale.salewar.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.antwhale.salewar.data.room.entity.FavoriteProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Query("DELETE FROM FavoriteProduct")
    suspend fun deleteAll()

    @Query("SELECT * FROM FavoriteProduct")
    suspend fun getAll() : List<FavoriteProduct>

    @Query("SELECT * FROM FavoriteProduct")
    fun getAllByFlow() : Flow<List<FavoriteProduct>>

    @Query("UPDATE favoriteproduct SET img = :newImg, price = :newPrice, saleFlag = :newSaleFlag WHERE title = :productTitle")
    suspend fun updateFavoriteProduct(productTitle: String, newImg: String, newPrice: String, newSaleFlag: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM FavoriteProduct WHERE title = :productTitle)")
    fun isFavoriteProduct(productTitle: String): Flow<Boolean>

    @Insert
    suspend fun addFavoriteProduct(product: FavoriteProduct)

    @Query("DELETE FROM FavoriteProduct WHERE title = :favoriteProductTitle")
    suspend fun deleteFavoriteProduct(favoriteProductTitle: String)
}