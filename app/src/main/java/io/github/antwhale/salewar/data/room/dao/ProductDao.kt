package io.github.antwhale.salewar.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.antwhale.salewar.data.room.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Query("DELETE FROM Product")
    suspend fun deleteAll()

    @Query("DELETE FROM Product WHERE store In (:store)")
    suspend fun deleteProductsByStore(store: String)

    @Query("SELECT * FROM Product")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE store IN (:store)")
    fun getProductsByStore(store: String): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE title LIKE '%' || :title || '%' AND store = :store")
    fun searchProductsByTitleAndStore(title: String, store: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE title = :productTitle LIMIT 1")
    suspend fun isSaleProduct(productTitle: String): Product?
}