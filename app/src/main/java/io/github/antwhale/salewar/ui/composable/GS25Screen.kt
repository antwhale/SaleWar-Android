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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.antwhale.salewar.data.room.entity.Product
import io.github.antwhale.salewar.ui.theme.SaleWarTheme
import io.github.antwhale.salewar.ui.theme.Yellow
import io.github.antwhale.salewar.viewmodel.GS25ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GS25Screen(modifier: Modifier, gs25ViewModel: GS25ViewModel) {
    val TAG = "GS25Screen"
    val gs25Products by gs25ViewModel.productList.collectAsState()
    val searchKeyword by gs25ViewModel.searchKeyword.collectAsState()

    val selectedProduct by gs25ViewModel.selectedProduct.collectAsState()
    val isSelectedProductFavorite by gs25ViewModel.isSelectedProductFavorite.collectAsState()

    val showingFavoriteList by gs25ViewModel.showingFavoriteList.collectAsState()
    val favoriteProducts by gs25ViewModel.favoriteProducts.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Allows the sheet to stop at a half-expanded state
    )

    Box {
        Box(Modifier.fillMaxWidth().fillMaxHeight(0.35f).align(Alignment.BottomCenter).background(Yellow)) {}

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))

            SaleWarTitleBar(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClickFavoriteMenu = {gs25ViewModel.showingFavoriteList.value = true}
            )

            Spacer(Modifier.height(16.dp))

            Text(style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold), text = "GS25의 할인상품을 만나보세요!")

            Spacer(Modifier.height(16.dp))

            SaleWarSearchBar(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onTextChanged = { text ->
                    gs25ViewModel.searchKeyword.value = text
                },
                searchKeyword
            )

            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(gs25Products) { product ->
                    ProductGridItem(
                        product = product,
                        onProductClicked = {gs25ViewModel.selectedProduct.value = product}
                    )
                }
            }

            selectedProduct?.let { product ->
                ProductDetailDialog(
                    product = product,
                    isFavorite = isSelectedProductFavorite,
                    // When the dialog is dismissed (e.g., by clicking the button), set the state back to null
                    onDismiss = { gs25ViewModel.selectedProduct.value = null },
                    onToggledFavorite = {
                        if(isSelectedProductFavorite) {
                            gs25ViewModel.deleteFavoriteProduct(product)
                        } else {
                            gs25ViewModel.addFavoriteProduct(product)
                        }
                    }
                )
            }

            if(showingFavoriteList){
                FavoriteProductList(
                    favoriteProducts = favoriteProducts,
                    sheetState = sheetState,
                    onDeleteFavoriteProduct = { product -> gs25ViewModel.deleteFavoriteProduct(Product(img = product.img, title = product.title, price = product.price, saleFlag = product.saleFlag, store = product.store))},
                    onDismiss = { gs25ViewModel.showingFavoriteList.value = false }
                )
            }
        }

    }

}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun GS25ScreenPreview() {
    SaleWarTheme {
//        GS25Screen(Modifier.fillMaxSize(), gs25ViewModel = GS25ViewModel())
    }
}