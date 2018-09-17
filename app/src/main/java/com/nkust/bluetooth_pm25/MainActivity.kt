package com.nkust.bluetooth_pm25

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
    }

    private fun setView() {
        buttonOpenScan.setOnClickListener {
            startActivity(Intent(this, ScanBluetoothActivity::class.java))
        }
    }
}
