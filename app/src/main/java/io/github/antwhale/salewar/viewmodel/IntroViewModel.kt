package io.github.antwhale.salewar.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.antwhale.salewar.MainActivity
import io.github.antwhale.salewar.data.room.RoomManager
import io.github.antwhale.salewar.data.room.entity.LastFetchInfo
import io.github.antwhale.salewar.data.vo.PRODUCT_VERSION_URL
import io.github.antwhale.salewar.data.vo.ProductJSON
import io.github.antwhale.salewar.data.vo.StoreType
import io.github.antwhale.salewar.data.vo.toProduct
import io.github.antwhale.salewar.BuildConfig
import io.github.antwhale.salewar.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date

@HiltViewModel
class IntroViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val TAG = "IntroViewModel"

    val fetchingFlag = MutableStateFlow(true)
    val updateFlag = MutableStateFlow(false)

    fun checkProductVersion() {
        Log.d(TAG, "checkProductVersion")

        viewModelScope.launch {
            RoomManager.initialize(application.applicationContext)

            val result = readUrlContent(PRODUCT_VERSION_URL)

            if(result.isNullOrEmpty()) {
                Log.d(TAG, "checkProductVersion FAILED")
            } else {
                Log.d(TAG, "checkProductVersion SUCCESS")

                val nowVersion = BuildConfig.VERSION_NAME
                val serverVersion = result.replace("\n", "")
                Log.d(TAG, "nowVersion: $nowVersion, serverVersion: $serverVersion")

                val needToUpdate = checkUpdate(nowVersion, serverVersion)
                Log.d(TAG, "needToUpdate: $needToUpdate")

                if(needToUpdate) {
                    //업데이트 팝업
                    updateFlag.value = true
                    return@launch

                } else {
                    val dbDate = RoomManager.getLastFetchDate()

                    val nowDate = Date()
                    val sdf = SimpleDateFormat("yyMM")
                    val currentDate = sdf.format(nowDate)

                    Log.d(TAG, "dbDate: $dbDate, currentDate: $currentDate")

                    if(dbDate != currentDate) {
                        Log.d(TAG, "Need to init Database")
                        initAllSaleInfo()
                    }

                }

                fetchingFlag.value = false
            }
        }
    }

    private suspend fun initAllSaleInfo() {
        Log.d(TAG, "initAllSaleInfo")

        withContext(Dispatchers.IO) {
            initSaleInfo(application, StoreType.GS25)
            initSaleInfo(application, StoreType.CU)
            initSaleInfo(application, StoreType.SEVEN_ELEVEN)
            updateFavoriteProducts()
        }
    }

    private suspend fun initSaleInfo(context: Context, storeType: StoreType) {
        Log.d(TAG, "initSaleInfo")

        try {
            val data = withContext(Dispatchers.IO) {
                context.resources.openRawResource(storeType.resourceId).use { inputStream ->
                    inputStream.readBytes()
                }
            }

            val json = Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                encodeDefaults = true
            }

            val productJSONs = json.decodeFromString<List<ProductJSON>>(String(data))
            val storeProducts = productJSONs.map { productJSON ->
                productJSON.toProduct(storeType.rawValue)
            }

            RoomManager.deleteProducts(storeType)
            RoomManager.addProducts(storeProducts)

            Log.d(TAG,"RoomDB update complete!")

            if (storeType == StoreType.SEVEN_ELEVEN) {
                // This function would need to be defined elsewhere in your code
                val nowDate = Date()
                val sdf = SimpleDateFormat("yyMM")
                val date = sdf.format(nowDate)

                RoomManager.deleteAllLastFetchInfo()
                RoomManager.saveSaleInfoUpdateDate(LastFetchInfo(date))
            }

//            val fetchedProducts = RoomManager.getProducts()
//            Log.d(TAG,"Products in Realm: ${fetchedProducts.size}")
        } catch (e: Exception) {
            Log.d(TAG,"Error decoding product data: ${e.localizedMessage}")
        }
    }

    suspend fun updateFavoriteProducts() {
        Log.d(TAG, "updateFavoriteProducts")

        val favoriteProductList = RoomManager.getFavoriteProducts()
        for(favoriteProduct in favoriteProductList) {
            val productToUpdate = RoomManager.isSaleProduct(favoriteProduct)

            if(productToUpdate != null) {
                RoomManager.updateFavoriteProduct(favoriteProduct.title, productToUpdate.img, productToUpdate.price, productToUpdate.saleFlag, productToUpdate.category, productToUpdate.description)
            } else {
                RoomManager.updateFavoriteProduct(favoriteProduct.title, favoriteProduct.img, favoriteProduct.price, "", favoriteProduct.category, favoriteProduct.description)
            }
        }
    }

    suspend fun readUrlContent(urlString: String): String? {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                // Create a URL object from the string
                val url = URL(urlString)

                // Open a connection
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                // Read the response from the input stream
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    content.append(line).append("\n")
                }

                // Close the streams and return the content
                reader.close()
                inputStream.close()

                content.toString()
            } catch (e: Exception) {
                // Log the exception for debugging purposes
                e.printStackTrace()
                null
            } finally {
                // Disconnect the connection
                connection?.disconnect()
            }
        }
    }
//
//    fun checkUpdate(currentDate: String, newDate: String): Boolean {
//        Log.d(TAG, "checkUpdate, currentDate: $currentDate newDate: $newDate count: ${currentDate.length} ${newDate.length}")
//        // Check if the input strings are valid.
//        if (currentDate.length != 4 || newDate.length != 4) {
//            println("checkUpdate, invalid input so return true")
//            return true // Handle invalid input
//        }
//
//        // Extract year and month components.
//        val currentYear = currentDate.substring(0, 2).toIntOrNull()
//        val currentMonth = currentDate.substring(2, 4).toIntOrNull()
//        val newYear = newDate.substring(0, 2).toIntOrNull()
//        val newMonth = newDate.substring(2, 4).toIntOrNull()
//
//        if (currentYear == null || currentMonth == null || newYear == null || newMonth == null) {
//            Log.d(TAG,"checkUpdate, can not divide YYMM so return true")
//            return true // Handle invalid numeric values
//        }
//
//        // Perform the comparison.
//        if (currentYear < newYear) {
//            Log.d(TAG,"checkUpdate, currentYear < newYear")
//            return false
//        } else if (currentYear > newYear) {
//            Log.d(TAG,"checkUpdate, currentYear > newYear")
//            return true
//        } else { // Years are equal, compare months.
//            if (currentMonth < newMonth) {
//                Log.d(TAG, "checkUpdate, currentMonth < newMonth")
//                return false
//            } else if (currentMonth > newMonth) {
//                Log.d(TAG,"checkUpdate, currentMonth > newMonth")
//                return true
//            } else {
//                Log.d(TAG,"checkUpdate, Years and months are equal")
//                return false
//            }
//        }
//    }

    fun checkUpdate(nowVersion: String, serverVersion: String): Boolean {
        // 1. 점(.)을 기준으로 문자열을 분리합니다.
        val nowUnits = nowVersion.split(".").map { it.toIntOrNull() ?: 0 }
        val serverUnits = serverVersion.split(".").map { it.toIntOrNull() ?: 0 }

        // 2. 두 버전 중 더 긴 리스트의 길이를 구합니다 (예: 1.0 vs 1.0.1 대응)
        val maxLength = maxOf(nowUnits.size, serverUnits.size)

        for (i in 0 until maxLength) {
            // 해당 자릿수에 값이 없으면 0으로 처리합니다.
            val nowVal = nowUnits.getOrElse(i) { 0 }
            val serverVal = serverUnits.getOrElse(i) { 0 }

            if (serverVal > nowVal) {
                return true  // 서버 버전이 더 높으면 즉시 true 반환
            } else if (serverVal < nowVal) {
                return false // 로컬 버전이 더 높으면(테스트용 등) false 반환
            }
        }

        // 모든 자릿수가 같다면 업데이트 불필요
        return false
    }
}