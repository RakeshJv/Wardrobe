package com.wardrobe.data_layer.dao


import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

import com.wardrobe.data_layer.BOTTOM_COL_ID
import com.wardrobe.data_layer.BOTTOM_TABLE_NAME
import com.wardrobe.model.BottomElement

/**
 * This interface is used for Bottom DB persistence and retrieval.
 */
@Dao
interface BottomDao {

    @Query("SELECT * from ${BOTTOM_TABLE_NAME}")
    fun getBottoms(): MutableList<BottomElement>

    @Query("SELECT * from ${BOTTOM_TABLE_NAME} WHERE ${BOTTOM_COL_ID} = :bottomId")
    fun getBottom(bottomId: String): BottomElement

    @Insert(onConflict = REPLACE)
    fun insert(bottomElement: BottomElement)

    @Update
    fun update(bottomElement: BottomElement)

    @Delete
    fun deleteBottom(bottomElement: BottomElement)

}
