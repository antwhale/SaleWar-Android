package io.github.antwhale.salewar.ui.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.antwhale.salewar.data.room.entity.Product
import io.github.antwhale.salewar.ui.theme.Yellow
import io.github.antwhale.salewar.viewmodel.SevenElevenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SevenElevenScreen(modifier: Modifier, sevenElevenViewModel: SevenElevenViewModel) {
    val TAG = "SevenElevenScreen"
    val sevenElevenProducts by sevenElevenViewModel.productList.collectAsState()
    val searchKeyword by sevenElevenViewModel.searchKeyword.collectAsState()

    val selectedProduct by sevenElevenViewModel.selectedProduct.collectAsState()
    val isSelectedProductFavorite by sevenElevenViewModel.isSelectedProductFavorite.collectAsState()

    val showingFavoriteList by sevenElevenViewModel.showingFavoriteList.collectAsState()
    val favoriteProducts by sevenElevenViewModel.favoriteProducts.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Allows the sheet to stop at a half-expanded state
    )

    Box(){
        Box(Modifier.fillMaxWidth().fillMaxHeight(0.35f).align(Alignment.BottomCenter).background(
            Yellow
        ))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))

            SaleWarTitleBar(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClickFavoriteMenu = {sevenElevenViewModel.showingFavoriteList.value = true}
            )

            Spacer(Modifier.height(16.dp))

            Text(style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold), text = "SevenEleven의 할인상품을 만나보세요!")

            Spacer(Modifier.height(16.dp))

            SaleWarSearchBar(
                Modifier
                    .fillMaxWidth(),
                onTextChanged = { text ->
                    sevenElevenViewModel.searchKeyword.value = text
                },
                searchKeyword
            )

            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(sevenElevenProducts) { product ->
                    ProductGridItem(
                        product = product,
                        onProductClicked = {sevenElevenViewModel.selectedProduct.value = product}
                    )
                }
            }

            selectedProduct?.let { product ->
                ProductDetailDialog(
                    product = product,
                    isFavorite = isSelectedProductFavorite,
                    // When the dialog is dismissed (e.g., by clicking the button), set the state back to null
                    onDismiss = { sevenElevenViewModel.selectedProduct.value = null },
                    onToggledFavorite = {
                        if(isSelectedProductFavorite) {
                            sevenElevenViewModel.deleteFavoriteProduct(product)
                        } else {
                            sevenElevenViewModel.addFavoriteProduct(product)
                        }
                    }
                )
            }

            if(showingFavoriteList){
                FavoriteProductList(
                    favoriteProducts = favoriteProducts,
                    sheetState = sheetState,
                    onDeleteFavoriteProduct = { product -> sevenElevenViewModel.deleteFavoriteProduct(Product(img = product.img, title = product.title, price = product.price, saleFlag = product.saleFlag, store = product.store))},
                    onDismiss = { sevenElevenViewModel.showingFavoriteList.value = false }
                )
            }
        }
    }

}