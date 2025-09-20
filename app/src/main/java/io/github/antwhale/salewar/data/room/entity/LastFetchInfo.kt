package io.github.antwhale.salewar.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LastFetchInfo(
    @PrimaryKey val date: String
) {
}