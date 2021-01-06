package com.wardrobe.data_layer.dao


import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.wardrobe.data_layer.TOP_COL_ID
import com.wardrobe.data_layer.TOP_TABLE_NAME
import com.wardrobe.model.TopElement

/**
 * This interface is used for Top DB persistence and retrieval.
 */
@Dao
interface TopDao {

    @Query("SELECT * from ${TOP_TABLE_NAME}")
    fun getTops(): MutableList<TopElement>

    @Query("SELECT * from ${TOP_TABLE_NAME} WHERE ${TOP_COL_ID} = :topId")
    fun getTop(topId: String): TopElement

    @Insert(onConflict = REPLACE)
    fun insert(topElement: TopElement)

    @Update
    fun update(topElement: TopElement)

    @Delete
    fun deleteTop(topElement: TopElement)

}
