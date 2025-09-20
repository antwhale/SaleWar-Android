package io.github.antwhale.salewar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.antwhale.salewar.data.room.RoomManager
import io.github.antwhale.salewar.data.room.entity.LastFetchInfo
import io.github.antwhale.salewar.data.vo.PRODUCT_VERSION_URL
import io.github.antwhale.salewar.data.vo.ProductJSON
import io.github.antwhale.salewar.data.vo.StoreType
import io.github.antwhale.salewar.data.vo.toProduct
import kotlinx.coroutines.Dispatchers
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

    init {
//        checkProductVersion()
    }

    fun checkProductVersion() {
        Log.d(TAG, "checkProductVersion")

        viewModelScope.launch {
            RoomManager.initialize(application.applicationContext)

            val result = readUrlContent(PRODUCT_VERSION_URL)

            if(result.isNullOrEmpty()) {
                Log.d(TAG, "checkProductVersion FAILED")
            } else {
                Log.d(TAG, "checkProductVersion SUCCESS")

                val newDate = RoomManager.getLastFetchDate()

                val serverDate = result.replace("\n", "")
                Log.d(TAG, "newDate: $newDate, serverDate: $serverDate")

                val needToUpdate = checkUpdate(serverDate, newDate)
                if(needToUpdate) {
                    initAllSaleInfo()
                } else {
                    Log.d(TAG, "Don't need to update sale info")
                    //메인화면 진입
                }

            }
        }
    }

    private suspend fun initAllSaleInfo() {
        Log.d(TAG, "initAllSaleInfo")

        withContext(Dispatchers.IO) {
            initSaleInfo(StoreType.GS25)
            initSaleInfo(StoreType.CU)
            initSaleInfo(StoreType.SEVEN_ELEVEN)
            updateFavoriteProducts()
        }
    }

    private suspend fun initSaleInfo(storeType: StoreType){
        Log.d(TAG, "initSaleInfo")

        try {
            val url = URL(storeType.rawJSONURL)

            Log.d(TAG, "make url object")

            val (data, response) = withContext(Dispatchers.IO) {
                val connection = url.openConnection() as java.net.HttpURLConnection
                connection.requestMethod = "GET"
                val inputStream = connection.inputStream
                val data = inputStream.readBytes()
                connection.disconnect()
                Pair(data, connection.responseCode)
            }
            Log.d(TAG, "connected with ${storeType.rawValue}")

            if (response !in 200..299) {
                Log.d(TAG, "HTTP Error: Invalid status code $response")
                return
            }

            val json = Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                encodeDefaults = true
                namingStrategy = kotlinx.serialization.json.JsonNamingStrategy.SnakeCase
            }

            val productJSONs = json.decodeFromString<List<ProductJSON>>(String(data))
            val storeProducts = productJSONs.map { productJSON ->
                productJSON.toProduct(storeType.rawValue)
            }

            RoomManager.deleteProducts(storeType)
            RoomManager.addProducts(storeProducts)

            Log.d(TAG,"RealmDB update complete!")

            if (storeType == StoreType.SEVEN_ELEVEN) {
                // This function would need to be defined elsewhere in your code
                val nowDate = Date()
                val sdf = SimpleDateFormat("yyMM")
                val date = sdf.format(nowDate)

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

    fun checkUpdate(currentDate: String, newDate: String): Boolean {
        Log.d(TAG, "checkUpdate, currentDate: $currentDate newDate: $newDate count: ${currentDate.length} ${newDate.length}")
        // Check if the input strings are valid.
        if (currentDate.length != 4 || newDate.length != 4) {
            println("checkUpdate, invalid input so return true")
            return true // Handle invalid input
        }

        // Extract year and month components.
        val currentYear = currentDate.substring(0, 2).toIntOrNull()
        val currentMonth = currentDate.substring(2, 4).toIntOrNull()
        val newYear = newDate.substring(0, 2).toIntOrNull()
        val newMonth = newDate.substring(2, 4).toIntOrNull()

        if (currentYear == null || currentMonth == null || newYear == null || newMonth == null) {
            Log.d(TAG,"checkUpdate, can not divide YYMM so return true")
            return true // Handle invalid numeric values
        }

        // Perform the comparison.
        if (currentYear < newYear) {
            Log.d(TAG,"checkUpdate, currentYear < newYear")
            return false
        } else if (currentYear > newYear) {
            Log.d(TAG,"checkUpdate, currentYear > newYear")
            return true
        } else { // Years are equal, compare months.
            if (currentMonth < newMonth) {
                Log.d(TAG, "checkUpdate, currentMonth < newMonth")
                return false
            } else if (currentMonth > newMonth) {
                Log.d(TAG,"checkUpdate, currentMonth > newMonth")
                return true
            } else {
                Log.d(TAG,"checkUpdate, Years and months are equal")
                return false
            }
        }
    }
}