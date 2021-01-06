package com.wardrobe.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import  com.wardrobe.R
import  com.wardrobe.data_layer.TOP_COL_FILE
import  com.wardrobe.data_layer.TOP_COL_ID
import  com.wardrobe.data_layer.TOP_TABLE_NAME

/**
 * This model represents a Top like a Shirt
 */
@Entity(tableName = TOP_TABLE_NAME)
class TopElement(
        @PrimaryKey @ColumnInfo(name = TOP_COL_ID) var id: String,
        @ColumnInfo(name = TOP_COL_FILE) var filePath: String)

    : WardrobeElement() {

    constructor() : this("null", "null")

    override fun getContent() = filePath

    override fun getPlaceholder() = R.drawable.shirt_placeholder

    override fun getType() = WardrobeType.TOP

}
