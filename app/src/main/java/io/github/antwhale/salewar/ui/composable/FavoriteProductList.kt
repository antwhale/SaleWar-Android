package io.github.antwhale.salewar.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.github.antwhale.salewar.R
import io.github.antwhale.salewar.data.room.entity.FavoriteProduct
import io.github.antwhale.salewar.data.vo.StoreType
import io.github.antwhale.salewar.ui.theme.OnePlusOneColor
import io.github.antwhale.salewar.ui.theme.TwoPlusOneColor
import kotlinx.coroutines.CoroutineScope
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteProductList(
    favoriteProducts: List<FavoriteProduct>,
    onDeleteFavoriteProduct: (FavoriteProduct) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            // This is called when the user taps outside or swipes down to dismiss.
            onDismiss()
        },
        sheetState = sheetState,
    ) {
        // --- Content of the Bottom Sheet ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(10.dp)
        ) {
            Text(
                text = "좋아요 목록",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(
                    items = favoriteProducts,
                    key = { product -> product.title } // Use a unique key for efficient list updates
                ) { product ->
                    // State for managing the dismiss action
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                // 1. Execute the deletion callback for the ViewModel/Repository
                                onDeleteFavoriteProduct(product)

                                // We handle the removal ourselves, so return true
                                return@rememberSwipeToDismissBoxState true
                            }
                            false
                        },
                        positionalThreshold = { 0.5f }
                    )

                    // Implement SwipeToDismiss
                    SwipeToDismissBox(
                        state = dismissState,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        backgroundContent = {
                            Text(
                                "Delete",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Red),
                            )
                        },
                        content = {
                            // This is the content that gets swiped (the list item itself)
                            FavoriteProductRow(product = product)
                        },
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavoriteProductRow(product: FavoriteProduct) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = product.img,
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp) // Adjusted size
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = product.price,
                    color = Color.Gray,
                    modifier = Modifier
                )

                Spacer(Modifier.height(8.dp))

                Row() {
                    Text(
                        text = if(product.saleFlag == "") "행사 상품이 아닙니다" else product.saleFlag,
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                color = saleFlagBackgroundColor(flag = product.saleFlag),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    )


                    Spacer(Modifier.weight(1f))

                    Image(modifier = Modifier.size(30.dp), painter = painterResource(id = StoreType.getBrandLogo(product.store)), contentDescription = product.store)
                }

            }


        }
    }
}

@Composable
private fun saleFlagBackgroundColor(flag: String): Color {
    return when (flag) {
        "1+1" -> OnePlusOneColor
        "2+1" -> TwoPlusOneColor
        else -> Color.Red
    }
}