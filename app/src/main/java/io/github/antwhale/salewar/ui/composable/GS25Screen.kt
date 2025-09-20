package io.github.antwhale.salewar.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.antwhale.salewar.ui.theme.SaleWarTheme
import io.github.antwhale.salewar.viewmodel.GS25ViewModel

@Composable
fun GS25Screen(modifier: Modifier, gs25ViewModel: GS25ViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))

        SaleWarTitleBar(Modifier.fillMaxWidth().wrapContentHeight())

        Spacer(Modifier.height(16.dp))

        Text(style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold), text = "GS25의 할인상품을 만나보세요!")

        Spacer(Modifier.height(16.dp))

        SaleWarSearchBar(Modifier.fillMaxWidth().height(45.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
//            items(gs25ViewModel.productList, key = { it.id }) { product ->
//                ProductGridItem(product = product, {}) {
//                    if (isSearchBarFocused) {
//                        onSearchBarFocusChange(false)
//                    } else {
//                        gs25ViewModel.showingProductDetailView = true
//                        gs25ViewModel.selectedProduct = product
//                    }
//                }
//            }
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