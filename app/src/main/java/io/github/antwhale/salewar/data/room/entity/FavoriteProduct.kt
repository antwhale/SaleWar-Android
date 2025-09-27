package io.github.antwhale.salewar.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "img") val img: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "saleFlag") val saleFlag: String,
    @ColumnInfo(name = "store") val store: String,
)
