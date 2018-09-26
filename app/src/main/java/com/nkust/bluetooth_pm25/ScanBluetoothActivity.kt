package com.nkust.bluetooth_pm25

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.inuker.bluetooth.library.Code.REQUEST_SUCCESS
import com.inuker.bluetooth.library.beacon.Beacon
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener
import com.inuker.bluetooth.library.search.SearchRequest
import com.inuker.bluetooth.library.search.SearchResult
import com.inuker.bluetooth.library.search.response.SearchResponse
import com.inuker.bluetooth.library.utils.BluetoothLog
import com.nkust.bluetooth_pm25.adapter.SearchResultAdapter
import com.nkust.bluetooth_pm25.base.BaseActivity
import kotlinx.android.synthetic.main.activity_scan_bluetooth.*


class ScanBluetoothActivity : BaseActivity() {

    private var isSearch = false

    private var searchResultAdapter: SearchResultAdapter? = null

    private val searchResultList: MutableList<SearchResult> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_bluetooth)
        setView()
    }

    private fun setView() {
        progressBar.visibility = View.GONE
        textState.text = if (baseApplication.mClient.isBluetoothOpened) "已開啟藍芽 可準備掃描" else "藍芽關閉 請點選開啟藍芽"
        buttonSearch.isEnabled = baseApplication.mClient.isBluetoothOpened
        searchResultAdapter = SearchResultAdapter(searchResultList, object : SearchResultAdapter.OnItemListener {
            override fun onItemClick(item: SearchResult, position: Int) {
                baseApplication.mClient.stopSearch()
                textState.text = "連接中"
                progressBar.visibility = View.VISIBLE
                baseApplication.mClient.connect(item.address) { code, profile ->
                    progressBar.visibility = View.GONE
                    if (code == REQUEST_SUCCESS) {
                        textState.text =
                                String.format("已成功連接 %s", item.name)
                        baseApplication.searchResult = item
                        baseApplication.profile = profile
                    } else
                        Toast.makeText(this@ScanBluetoothActivity, "失敗", Toast.LENGTH_SHORT).show()
                }
            }
        })
        recyclerViewScanResult.layoutManager = LinearLayoutManager(this)
        recyclerViewScanResult.adapter = searchResultAdapter
        val request = SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build()
        buttonOpenBluetooth.setOnClickListener {
            baseApplication.mClient.openBluetooth()
        }
        buttonBackHome.setOnClickListener { finish() }
        buttonSearch.setOnClickListener {
            if (!isSearch)
                baseApplication.mClient.search(request, object : SearchResponse {
                    override fun onSearchStarted() {
                        searchResultList.clear()
                        searchResultAdapter?.notifyDataSetChanged()
                        textState.text = "掃描中..."
                        buttonSearch.text = "停止掃描"
                        progressBar.visibility = View.VISIBLE
                        isSearch = true
                    }

                    override fun onDeviceFounded(device: SearchResult) {
                        val beacon = Beacon(device.scanRecord)
                        BluetoothLog.v(String.format("beacon for %s\n%s", device.address, beacon.toString()))
                        if (device.name != "NULL")
                            if (searchResultList.filter { it.address == device.address }.isEmpty())
                                searchResultList.add(device)
                        searchResultAdapter?.notifyDataSetChanged()
                    }

                    override fun onSearchStopped() {
                        textState.text = if (baseApplication.mClient.isBluetoothOpened) "已開啟藍芽 可準備掃描" else "藍芽關閉 請點選開啟藍芽"
                        buttonSearch.text = "開始掃描"
                        progressBar.visibility = View.GONE
                        isSearch = false
                    }

                    override fun onSearchCanceled() {
                        textState.text = if (baseApplication.mClient.isBluetoothOpened) "已開啟藍芽 可準備掃描" else "藍芽關閉 請點選開啟藍芽"
                        buttonSearch.text = "開始掃描"
                        progressBar.visibility = View.GONE
                        isSearch = false
                    }
                })
            else
                baseApplication.mClient.stopSearch()
        }
        baseApplication.mClient.registerBluetoothStateListener(object : BluetoothStateListener() {
            override fun onBluetoothStateChanged(openOrClosed: Boolean) {
                textState.text = if (openOrClosed) "已開啟藍芽 可準備掃描" else "藍芽關閉 請點選開啟藍芽"
                buttonSearch.isEnabled = openOrClosed
            }
        })
    }
}
