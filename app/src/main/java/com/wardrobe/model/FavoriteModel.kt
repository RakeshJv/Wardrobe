package com.wardrobe.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wardrobe.data_layer.FAVORITE_COL_BOTTOM_ID
import com.wardrobe.data_layer.FAVORITE_COL_ID
import com.wardrobe.data_layer.FAVORITE_COL_TOP_ID
import com.wardrobe.data_layer.FAVORITE_TABLE_NAME

/**
 * This class represents a Favorite item - a combination of Top & Bottom.
 */
@Entity(tableName = FAVORITE_TABLE_NAME)
data class FavoriteModel(
        @PrimaryKey @ColumnInfo(name = FAVORITE_COL_ID) var id: String,
        @ColumnInfo(name = FAVORITE_COL_TOP_ID) var topId: String,
        @ColumnInfo(name = FAVORITE_COL_BOTTOM_ID) var bottomId: String) {

    constructor() : this("null", "null", "null")
}
