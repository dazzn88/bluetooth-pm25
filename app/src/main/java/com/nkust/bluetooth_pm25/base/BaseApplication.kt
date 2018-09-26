package com.nkust.bluetooth_pm25.base

import android.app.Application
import com.inuker.bluetooth.library.BluetoothClient
import com.inuker.bluetooth.library.model.BleGattProfile
import com.inuker.bluetooth.library.search.SearchResult

/**
 * Created by Ray on 2017/8/22.
 */

class BaseApplication : Application() {

    lateinit var mClient: BluetoothClient
    var searchResult: SearchResult? = null
    lateinit var profile: BleGattProfile

    override fun onCreate() {
        super.onCreate()
        mClient = BluetoothClient(this)
    }
}
