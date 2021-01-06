package com.wardrobe.app

import android.app.Application
import android.content.Context
import com.wardrobe.data_layer.WardrobeDatabase
import com.wardrobe.data_layer.DataRepo

/**
 * Application class.
 *
 * Initialise all required 3rd party libraries and utility classes here.
 */
class WardrobeApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
        initDatabase()
    }

    private fun initDatabase() {
        WardrobeDatabase.init(this)
        DataRepo.initialise()
    }
}
