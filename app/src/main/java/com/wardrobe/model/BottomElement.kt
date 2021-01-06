package com.wardrobe.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import  com.wardrobe.data_layer.BOTTOM_TABLE_NAME
import  com.wardrobe.R
import  com.wardrobe.data_layer.BOTTOM_COL_FILE
import  com.wardrobe.data_layer.BOTTOM_COL_ID
import  com.wardrobe.data_layer.BOTTOM_TABLE_NAME

/**
 * This class represents a Bottom - like a Pant.
 */
@Entity(tableName = BOTTOM_TABLE_NAME)
class BottomElement(
        @PrimaryKey @ColumnInfo(name = BOTTOM_COL_ID) var id: String,
        @ColumnInfo(name = BOTTOM_COL_FILE) var filePath: String)

    : WardrobeElement() {

    constructor() : this("null", "null")

    override fun getContent() = filePath

    override fun getPlaceholder() = R.drawable.bottom_placeholder

    override fun getType() = WardrobeType.BOTTOM

}
