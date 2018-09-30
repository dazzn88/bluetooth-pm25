package com.nkust.bluetooth_pm25

import android.content.Intent
import android.os.Bundle
import com.nkust.bluetooth_pm25.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
    }

    private fun setView() {
        buttonOpenScan.setOnClickListener {
            startActivityForResult(Intent(this, ScanBluetoothActivity::class.java), 1)
        }
        buttonOpenPm25Data.setOnClickListener {
            startActivityForResult(Intent(this, Pm25DataActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
