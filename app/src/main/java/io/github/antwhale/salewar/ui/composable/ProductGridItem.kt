package io.github.antwhale.salewar.ui.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.github.antwhale.salewar.data.room.entity.Product
import io.github.antwhale.salewar.ui.theme.OnePlusOneColor
import io.github.antwhale.salewar.ui.theme.TwoPlusOneColor
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductGridItem(
    product: Product,
    onProductClicked: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .padding(8.dp)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple(), onClick = {onProductClicked.invoke()}),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),


    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Async image loading using Coil
            GlideImage(
                model = product.img,
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = product.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = product.price,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = product.saleFlag,
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = saleFlagBackgroundColor(flag = product.saleFlag),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun saleFlagBackgroundColor(flag: String): Color {
    return when (flag) {
        "1+1" -> OnePlusOneColor
        "2+1" -> TwoPlusOneColor
        else -> Color.Gray
    }
}