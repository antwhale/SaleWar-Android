package io.github.antwhale.salewar.ui.composable

import android.graphics.drawable.Icon
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.github.antwhale.salewar.data.room.entity.Product
import io.github.antwhale.salewar.ui.theme.OnePlusOneColor
import io.github.antwhale.salewar.ui.theme.TwoPlusOneColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailDialog(
    product: Product?,
    isFavorite: Boolean,
    onDismiss: () -> Unit,
    onToggledFavorite: (Product) -> Unit
) {
    // Only show the view if a product is selected
    if (product == null) return

    // Use a Dialog to handle the modal nature and background dimming
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Allows custom width/height for the content
        )
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val ripple = rememberRipple()

        // Replicates the full-screen overlay (Color.black.opacity(0.45))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {
            // Main content card (Replicating the inner ZStack/Card structure)
            // Intercepts click so background click doesn't close the dialog
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // Approx width: geometry.size.width - 16
                    .fillMaxHeight(0.33f) // Approx height: geometry.size.height * 0.33
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .padding(16.dp),
//                contentAlignment = Alignment.Center,
            ) {
                // Content (HStack equivalent)
                Row (
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Side: Image
                    GlideImage(
                        model = product.img,
                        contentDescription = product.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp) // Adjusted size
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(Modifier.width(16.dp))

                    // Right Side: Details (VStack equivalent)
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Title
                        Text(
                            text = product.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(8.dp))

                        // Price
                        Text(
                            text = product.price,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(8.dp))

                        // Sale Flag
                        Text(
                            text = product.saleFlag,
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    color = saleFlagBackgroundColor(product.saleFlag),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        )

                        Spacer(Modifier.height(8.dp))

                        // Favorite Icon
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Toggle Favorite",
                            // Using a vibrant pink/red tint for favorite
                            tint = if (isFavorite) Color(0xFFFF4081) else Color.Gray,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable(interactionSource = interactionSource, indication = ripple, onClick = { onToggledFavorite(product) })
                        )
                    }
                }

                // Close Button (ZStack alignment: topTrailing equivalent)
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(30.dp)
                        .padding(top = 4.dp, end = 4.dp)
                        .clickable(interactionSource = interactionSource, indication = ripple, onClick = onDismiss)
                )
            }
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