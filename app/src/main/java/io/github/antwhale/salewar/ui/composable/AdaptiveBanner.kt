package io.github.antwhale.salewar.ui.composable

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

//고정형 배너
//@Composable
//fun AdvertView(modifier: Modifier = Modifier) {
//    AndroidView(
//        modifier = modifier.fillMaxWidth(),
//        factory = { context ->
//            // AdView 객체 생성 및 설정
//            AdView(context).apply {
//                setAdSize(AdSize.BANNER)
//                // 테스트용 배너 ID (실제 출시 시 본인 ID로 교체)
//                adUnitId = "ca-app-pub-3940256099942544/6300978111"
//                loadAd(AdRequest.Builder().build())
//            }
//        },
//        update = { adView ->
//            // 필요한 경우 업데이트 로직 (보통은 비워둠)
//        }
//    )
//}

//적응형 배너
@Composable
fun AdaptiveBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { ctx ->
            AdView(ctx).apply {
                // 화면 너비에 맞는 적응형 사이즈 계산
                val adSize = getAdSize(context)
                setAdSize(adSize)
                adUnitId = "ca-app-pub-3757171409537383/4066536420"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

// 화면 너비를 계산하는 헬퍼 함수
private fun getAdSize(context: Context): AdSize {
    val displayMetrics = context.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels
    val density = displayMetrics.density
    val adWidth = (widthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}
