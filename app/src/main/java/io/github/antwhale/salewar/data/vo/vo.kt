package io.github.antwhale.salewar.data.vo

import io.github.antwhale.salewar.R

enum class SaleWarTab {
    gs25, cu, seven_eleven
}

const val GS25_PRODUCT_URL = "https://raw.githubusercontent.com/antwhale/SaleWar/refs/heads/main/GS25_Product.json"
const val CU_PRODUCT_URL = "https://raw.githubusercontent.com/antwhale/SaleWar/refs/heads/main/CU_Product.json"
const val SEVEN_ELEVEN_PRODUCT_URL = "https://raw.githubusercontent.com/antwhale/SaleWar/refs/heads/main/SevenEleven_Product.json"
const val PRODUCT_VERSION_URL = "https://raw.githubusercontent.com/antwhale/SaleWar/refs/heads/main/product_version"


enum class StoreType(val rawValue: String) {
    GS25("GS25"),
    CU("CU"),
    SEVEN_ELEVEN("SevenEleven");

    val rawJSONURL: String
        get() = when (this) {
            GS25 -> "https://raw.githubusercontent.com/antwhale/SaleWar/main/GS25_Product.json"
            CU -> "https://raw.githubusercontent.com/antwhale/SaleWar/main/CU_Product.json"
            SEVEN_ELEVEN -> "https://raw.githubusercontent.com/antwhale/SaleWar/main/SevenEleven_Product.json"
        }

    val brandLogo: Int
        get() = when (this) {
            GS25 -> R.drawable.gs25_logo
            CU -> R.drawable.cu_logo
            SEVEN_ELEVEN -> R.drawable.seven_eleven_logo
        }

    companion object {
        /**
         * The companion object replaces the Swift static functions.
         * This function returns the brand logo based on a given store name string.
         */
        fun getBrandLogo(from: String): Int {
            return when (from) {
                GS25.rawValue -> GS25.brandLogo
                CU.rawValue -> CU.brandLogo
                SEVEN_ELEVEN.rawValue -> SEVEN_ELEVEN.brandLogo
                else -> GS25.brandLogo // Default case for unknown store names.
            }
        }
    }
}

data class ProductJSON(
    val img: String,
    val title: String,
    val price: String, // String to handle "4,000원"
    val saleFlag: String
)

data class ProductInfo(
    val img: String,
    val title: String,
    val price: String,
    val saleFlag: String,
    val store: String
)

fun ProductJSON.toProductInfo(store: String): ProductInfo {
    return ProductInfo(
        img = this.img,
        title = this.title,
        price = this.price,
        saleFlag = this.saleFlag,
        store = store
    )
}

//data class Product(
//    val img: String,
//    val title: String,
//    val price: String,
//    val saleFlag: String
//)