package io.github.antwhale.salewar.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import io.github.antwhale.salewar.data.room.entity.FavoriteProduct

@Dao
interface FavoriteProductDao {
    @Query("DELETE FROM FavoriteProduct")
    suspend fun deleteAll()

    @Query("SELECT * FROM Product")
    suspend fun getAll() : List<FavoriteProduct>

    @Query("UPDATE favoriteproduct SET img = :newImg, price = :newPrice, saleFlag = :newSaleFlag WHERE title = :productTitle")
    suspend fun updateFavoriteProduct(productTitle: String, newImg: String, newPrice: String, newSaleFlag: String): Int

}