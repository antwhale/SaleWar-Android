package io.github.antwhale.salewar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.antwhale.salewar.data.room.RoomManager
import io.github.antwhale.salewar.data.room.entity.Product
import io.github.antwhale.salewar.data.vo.StoreType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SevenElevenViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val TAG = "SevenElevenViewModel"

    val searchKeyword = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val productList: StateFlow<List<Product>> = searchKeyword
        .debounce(300)
        .flatMapLatest { keyword ->
            Log.d(TAG, "Executing search query for keyword: $keyword")

            if(keyword.isEmpty()) {
                RoomManager.getProductsByStore(StoreType.SEVEN_ELEVEN.rawValue)
            } else {
                RoomManager.searchProductsByTitleAndStore(
                    keyword = keyword,
                    store = StoreType.SEVEN_ELEVEN.rawValue
                )
            }

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val selectedProduct = MutableStateFlow<Product?>(null)

}