package com.wardrobe.data_layer


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wardrobe.app.DATABASE_IN_MEMORY_TEST
import com.wardrobe.data_layer.DbThread
import com.wardrobe.data_layer.dao.BottomDao
import  com.wardrobe.data_layer.dao.FavoriteDao
import com.wardrobe.data_layer.dao.TopDao
import com.wardrobe.model.BottomElement
import com.wardrobe.model.FavoriteModel
import com.wardrobe.model.TopElement

@Database(entities = [(TopElement::class), (BottomElement::class), (FavoriteModel::class)], version = 1)
abstract class WardrobeDatabase : RoomDatabase() {

    abstract fun topDao(): TopDao
    abstract fun bottomDao(): BottomDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private const val TAG = "WardrobeDatabase"

        private lateinit var INSTANCE: WardrobeDatabase
        lateinit var dbThread: DbThread

        /**
         * Dao methods for fetching DB operation objects.
         */
        fun topDao() = INSTANCE.topDao()
        fun bottomDao() = INSTANCE.bottomDao()
        fun favoriteDao() = INSTANCE.favoriteDao()

        /**
         * Initialise the Room database.
         * Lazily initialised from Application.
         */
        fun init(context: Context) {
            INSTANCE = getDatabase(context.applicationContext, WardrobeDatabase::class.java, DATABASE_NAME)
                    .build()
            dbThread = DbThread(threadName = "DbThread")
            dbThread.start()
        }

        private fun <T : RoomDatabase> getDatabase(appContext: Context, klass: Class<T>, dbName: String): RoomDatabase.Builder<T> {
            return if (DATABASE_IN_MEMORY_TEST)
                Room.inMemoryDatabaseBuilder(appContext, klass)
            else
                Room.databaseBuilder(appContext, klass, dbName)
        }
    }
}
