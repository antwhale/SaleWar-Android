package io.github.antwhale.salewar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.application
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.antwhale.salewar.ui.theme.SaleWarTheme
import io.github.antwhale.salewar.viewmodel.IntroViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroActivity : ComponentActivity() {
    val TAG = "IntroActivity"
    private val introViewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        introViewModel.checkProductVersion()

        lifecycleScope.launch {
            introViewModel.fetchingFlag
                .drop(1)
                .collect{ fetching ->
                    if(fetching == false) {
                        goToMainActivity()
                    }
                }
        }

        enableEdgeToEdge()
        setContent {
            SaleWarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(modifier = Modifier.background(Color.White), contentAlignment = Alignment.Center) {
                        Image(modifier = Modifier.size(200.dp), painter = painterResource(R.drawable.ic_salewar), contentDescription = "app_logo")
                    }
                }
            }
        }
    }

    private fun goToMainActivity() {
        Log.d(TAG, "goToMainActivity")

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(this, intent, null)
        finish()
    }
}