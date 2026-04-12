package io.github.antwhale.salewar

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.application
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.room.Update
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
            val updateFlag by introViewModel.updateFlag.collectAsStateWithLifecycle()

            SaleWarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if(updateFlag) {
                        UpdateDialog(
                            onConfirm =  {
                                goToPlayStore()
                            }
                        )
                    } else {
                        Box(modifier = Modifier.background(Color.White), contentAlignment = Alignment.Center) {
                            Image(modifier = Modifier.size(200.dp), painter = painterResource(R.drawable.ic_salewar), contentDescription = "app_logo")
                        }
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

    private fun goToPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=$packageName")
            // 플레이스토어 앱이 없을 경우를 대비해 브라우저 실행 허용
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    @Composable
    fun UpdateDialog(
        onConfirm: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = {
                // 다이얼로그 바깥을 눌렀을 때 처리 (강제 업데이트라 비워둠)
            },
            title = {
                Text(text = "업데이트")
            },
            text = {
                Text(text = "최신버전의 앱을 다운로드해주세요")
            },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("확인")
                }
            }
        )
    }
}