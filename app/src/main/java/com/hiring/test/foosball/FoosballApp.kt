package com.hiring.test.foosball

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoosballApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
