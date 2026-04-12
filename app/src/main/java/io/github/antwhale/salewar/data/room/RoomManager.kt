package io.github.antwhale.salewar.data.room

import android.content.Context
import android.util.Log
import androidx.room.Room
import io.github.antwhale.salewar.data.room.dao.FavoriteProductDao
import io.github.antwhale.salewar.data.room.dao.LastFetchInfoDao
import io.github.antwhale.salewar.data.room.dao.ProductDao
import io.github.antwhale.salewar.data.room.entity.FavoriteProduct
import io.github.antwhale.salewar.data.room.entity.LastFetchInfo
import io.github.antwhale.salewar.data.room.entity.Product
import io.github.antwhale.salewar.data.vo.StoreType
import kotlinx.coroutines.flow.Flow

object RoomManager {
    val TAG = "RoomManager"
    private lateinit var db : AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var lastFetchInfoDao: LastFetchInfoDao
    private lateinit var favoriteProductDao: FavoriteProductDao

    fun initialize(context: Context) {
        Log.d(TAG, "initialize")
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
        productDao = db.productDao()
        lastFetchInfoDao = db.lastFetchInfoDao()
        favoriteProductDao = db.favoriteProductDao()
    }

    suspend fun getLastFetchDate() : String {
        val lastFetchInfos = lastFetchInfoDao.getAll()
        Log.d(TAG, "lastFetchInfos: $lastFetchInfos")
        if(lastFetchInfos.isNotEmpty()) {
            return lastFetchInfos[0].date
        } else {
            return ""
        }
    }

    suspend fun deleteAllLastFetchInfo() {
        Log.d(TAG, "deleteAllLastFetchInfo")
        lastFetchInfoDao.deleteAll()
    }


    fun getProductsByStore(store: String): Flow<List<Product>>{
        return productDao.getProductsByStore(store)
    }

    fun getProductsByStoreAndCategory(store: String, category: String): Flow<List<Product>> {
        return productDao.getProductsByStoreAndCategory(store, category)
    }
    fun searchProductsByTitleAndStore(keyword: String , store: String) : Flow<List<Product>> {
        return productDao.searchProductsByTitleAndStore(
            title = keyword,
            store = store
        )
    }

    fun searchProducts(keyword: String, store: String, category: String): Flow<List<Product>> {
        return productDao.searchProducts(keyword, store, category)
    }

    suspend fun deleteProducts(forStore: StoreType) {
        productDao.deleteProductsByStore(forStore.rawValue)
    }

    suspend fun addProducts(products: List<Product>) {
        productDao.insertProducts(products)
    }

    suspend fun getFavoriteProducts() : List<FavoriteProduct> {
        return favoriteProductDao.getAll()
    }

    fun getFavoriteProductsByFlow() : Flow<List<FavoriteProduct>> = favoriteProductDao.getAllByFlow()


    fun isFavoriteProduct(productTitle: String) : Flow<Boolean> {
        return favoriteProductDao.isFavoriteProduct(productTitle)
    }

    suspend fun isSaleProduct(product: FavoriteProduct) : Product? {
        return productDao.isSaleProduct(product.title)
    }

    suspend fun getProductCategoriesByStore(store: String) : List<String> {
        return productDao.getProductCategoriesByStore(store)
    }
    suspend fun saveSaleInfoUpdateDate(info: LastFetchInfo){
        Log.d(TAG, "saveSaleInfoUpdateDate, date: ${info.date}")
        lastFetchInfoDao.insertLastFetchInfo(info)
    }

    suspend fun updateFavoriteProduct(productTitle: String, newImg: String, newPrice: String, newSaleFlag: String, category: String, description: String) {
        favoriteProductDao.updateFavoriteProduct(productTitle, newImg, newPrice, newSaleFlag, category, description)
    }

    suspend fun addFavoriteProduct(product: FavoriteProduct) {
        favoriteProductDao.addFavoriteProduct(product)
    }

    suspend fun deleteFavoriteProduct(favoriteProductTitle: String) {
        favoriteProductDao.deleteFavoriteProduct(favoriteProductTitle)
    }

    fun release() {
        Log.d(TAG, "release: ")
        db.close()
    }
}