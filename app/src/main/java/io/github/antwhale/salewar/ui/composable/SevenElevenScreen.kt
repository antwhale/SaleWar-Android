package io.github.antwhale.salewar.ui.composable

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.antwhale.salewar.viewmodel.SevenElevenViewModel

@Composable
fun SevenElevenScreen(modifier: Modifier, sevenElevenViewModel: SevenElevenViewModel) {
    val TAG = "SevenElevenScreen"
    val sevenElevenProducts by sevenElevenViewModel.productList.collectAsState()
    val searchKeyword by sevenElevenViewModel.searchKeyword.collectAsState()

    val selectedProduct by sevenElevenViewModel.selectedProduct.collectAsState()
    val isSelectedProductFavorite by sevenElevenViewModel.isSelectedProductFavorite.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))

        SaleWarTitleBar(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onClickFavoriteMenu = {}
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
            horizontalArrangement = Arrangement.spacedBy(15.dp)
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
    }
}