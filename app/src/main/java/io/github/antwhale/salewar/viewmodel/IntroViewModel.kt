package io.github.antwhale.salewar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.antwhale.salewar.data.room.RoomManager
import io.github.antwhale.salewar.data.vo.PRODUCT_VERSION_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val TAG = "IntroViewModel"

    init {
        checkProductVersion()
    }

    private fun checkProductVersion() {
        Log.d(TAG, "checkProductVersion")

        viewModelScope.launch {
            RoomManager.initialize(application.applicationContext)

            val result = readUrlContent(PRODUCT_VERSION_URL)

            if(result.isNullOrEmpty()) {
                Log.d(TAG, "checkProductVersion FAILED")
            } else {
                Log.d(TAG, "checkProductVersion SUCCESS")

                val newDate = RoomManager.getLastFetchDate()[0].date
                val serverDate = result.replace("\n", "")
                Log.d(TAG, "newDate: $newDate, serverDate: $serverDate")
//
//

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
}