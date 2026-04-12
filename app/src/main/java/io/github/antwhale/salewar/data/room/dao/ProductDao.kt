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

    @Query("SELECT * FROM Product WHERE store = :store AND category = :category")
    fun getProductsByStoreAndCategory(store: String, category: String): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE title LIKE '%' || :title || '%' AND store = :store")
    fun searchProductsByTitleAndStore(title: String, store: String): Flow<List<Product>>

    /**
     * 제목 검색어, 카테고리, 편의점 정보를 모두 만족하는 상품 목록을 조회합니다.
     * 실시간 업데이트를 위해 Flow를 반환합니다.
     */
    @Query("""
        SELECT * FROM Product 
        WHERE title LIKE '%' || :title || '%' 
        AND category = :category 
        AND store = :store
    """)
    fun searchProducts(title: String, store: String, category: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE title = :productTitle LIMIT 1")
    suspend fun isSaleProduct(productTitle: String): Product?

    /**
     * 추가된 함수: 특정 편의점(store)의 카테고리 목록만 중복 없이 가져오고 싶을 때 사용하세요.
     */
    @Query("SELECT DISTINCT category FROM Product WHERE store = :store")
    suspend fun getProductCategoriesByStore(store: String): List<String>
}