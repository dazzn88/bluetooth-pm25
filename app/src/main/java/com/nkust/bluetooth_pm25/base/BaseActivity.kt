package com.nkust.bluetooth_pm25.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    lateinit var baseApplication: BaseApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseApplication =
                application as BaseApplication
    }
}
