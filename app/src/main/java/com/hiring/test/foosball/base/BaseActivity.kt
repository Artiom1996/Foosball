package com.hiring.test.foosball.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("launch ${this.javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("destroy ${this.javaClass.simpleName}")
    }
}
