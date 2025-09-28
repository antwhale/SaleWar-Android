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
import io.github.antwhale.salewar.viewmodel.CUViewModel

@Composable
fun CUScreen(modifier: Modifier, cuViewModel: CUViewModel) {
    val TAG = "CUScreen"
    val cuProducts by cuViewModel.productList.collectAsState()
    val searchKeyword by cuViewModel.searchKeyword.collectAsState()

    val selectedProduct by cuViewModel.selectedProduct.collectAsState()
    val isSelectedProductFavorite by cuViewModel.isSelectedProductFavorite.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))

        SaleWarTitleBar(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onClickFavoriteMenu = {}
        )

        Spacer(Modifier.height(16.dp))

        Text(style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold), text = "CU의 할인상품을 만나보세요!")

        Spacer(Modifier.height(16.dp))

        SaleWarSearchBar(
            Modifier
                .fillMaxWidth(),
            onTextChanged = { text ->
                cuViewModel.searchKeyword.value = text
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
            items(cuProducts) { product ->
                ProductGridItem(
                    product = product,
                    onProductClicked = {cuViewModel.selectedProduct.value = product}
                )
            }
        }

        selectedProduct?.let { product ->
            ProductDetailDialog(
                product = product,
                isFavorite = isSelectedProductFavorite,
                // When the dialog is dismissed (e.g., by clicking the button), set the state back to null
                onDismiss = { cuViewModel.selectedProduct.value = null },
                onToggledFavorite = {
                    if(isSelectedProductFavorite) {
                        cuViewModel.deleteFavoriteProduct(product)
                    } else {
                        cuViewModel.addFavoriteProduct(product)
                    }
                }
            )
        }
    }

}