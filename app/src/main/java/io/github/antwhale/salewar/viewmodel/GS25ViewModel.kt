package io.github.antwhale.salewar.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.key
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.antwhale.salewar.data.room.RoomManager
import io.github.antwhale.salewar.data.room.entity.FavoriteProduct
import io.github.antwhale.salewar.data.room.entity.Product
import io.github.antwhale.salewar.data.vo.StoreType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GS25ViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val TAG = "GS25ViewModel"

    val searchKeyword = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("")

    val gs25ProductCategories : MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val productList: StateFlow<List<Product>> = combine(
        searchKeyword.debounce(300),
        selectedCategory
    ) { (keyword, category) ->
        Pair(keyword, category)
    }.flatMapLatest { (keyword, category) ->
            Log.d(TAG, "Executing search query for keyword: $keyword, selectedCategory: $category")

            if(keyword.isEmpty()) {
                if(category.isEmpty() || category == "전체"){
                    RoomManager.getProductsByStore(StoreType.GS25.rawValue)
                } else {
                    RoomManager.getProductsByStoreAndCategory(StoreType.GS25.rawValue, category)
                }
            } else {
                if(category.isEmpty() || category == "전체") {
                    RoomManager.searchProductsByTitleAndStore(
                        keyword = keyword,
                        store = StoreType.GS25.rawValue
                    )
                } else {
                    RoomManager.searchProducts(keyword, StoreType.GS25.rawValue, category)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val selectedProduct = MutableStateFlow<Product?>(null)

    suspend fun fetchGS25Categories() {
        Log.d(TAG, "fetchGS25Categories")
        val categories = listOf("전체") + RoomManager.getProductCategoriesByStore(StoreType.GS25.rawValue)
        gs25ProductCategories.value = categories
        selectCategory("전체")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val isSelectedProductFavorite = selectedProduct
        .flatMapLatest { product ->
            RoomManager.isFavoriteProduct(product?.title ?: "")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    val showingFavoriteList = MutableStateFlow(false)

    val favoriteProducts = RoomManager
        .getFavoriteProductsByFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addFavoriteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "updateFavoriteProduct, ${product.title}")
            RoomManager.addFavoriteProduct(
                FavoriteProduct(img = product.img, title = product.title, price = product.price, saleFlag = product.saleFlag, store = product.store, category = product.category, description = product.description)
            )
        }
    }

    fun deleteFavoriteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "deleteFavoriteProduct, ${product.title}")
            RoomManager.deleteFavoriteProduct(product.title)
        }
    }

    fun selectCategory(category: String) {
        Log.d(TAG, "selectCategory: $category")
        selectedCategory.value = category
    }

    init {
        Log.d(TAG, "init")

    }


}