package com.nkust.bluetooth_pm25

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.inuker.bluetooth.library.BluetoothClient
import com.inuker.bluetooth.library.beacon.Beacon
import com.inuker.bluetooth.library.search.SearchRequest
import com.inuker.bluetooth.library.search.SearchResult
import com.inuker.bluetooth.library.search.response.SearchResponse
import com.inuker.bluetooth.library.utils.BluetoothLog
import com.nkust.bluetooth_pm25.adapter.SearchResultAdapter
import kotlinx.android.synthetic.main.activity_scan_bluetooth.*

class ScanBluetoothActivity : AppCompatActivity() {

    private lateinit var mClient: BluetoothClient

    private var searchResultAdapter: SearchResultAdapter? = null

    private val searchResultList: MutableList<SearchResult> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_bluetooth)
        setView()
    }

    private fun setView() {
        mClient = BluetoothClient(this)
        searchResultAdapter = SearchResultAdapter(searchResultList, object : SearchResultAdapter.OnItemListener {
            override fun onItemClick(item: SearchResult, position: Int) {
                Toast.makeText(this@ScanBluetoothActivity, position.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        recyclerViewScanResult.layoutManager = LinearLayoutManager(this)
        recyclerViewScanResult.adapter = searchResultAdapter
        val request = SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build()
        mClient.search(request, object : SearchResponse {
            override fun onSearchStarted() {

            }

            override fun onDeviceFounded(device: SearchResult) {
                val beacon = Beacon(device.scanRecord)
                BluetoothLog.v(String.format("beacon for %s\n%s", device.address, beacon.toString()))
                searchResultList.add(device)
                searchResultAdapter?.notifyDataSetChanged()
            }

            override fun onSearchStopped() {

            }

            override fun onSearchCanceled() {

            }
        })
    }
}
