package com.nkust.bluetooth_pm25

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.inuker.bluetooth.library.Code.REQUEST_SUCCESS
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
        buttonSend.setOnClickListener {
            baseApplication.searchResult?.apply {
                baseApplication.mClient.writeNoRsp(address,
                        baseApplication.profile.services[0].uuid,
                        baseApplication.profile.services[0].characters[0].uuid, editSendMessage.text.toString().toByteArray()) { code ->
                    if (code == REQUEST_SUCCESS) {
                        textReceive.append("\n")
                        textReceive.append("送出成功")
                    } else {
                        textReceive.append("\n")
                        textReceive.append("送出失敗")
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        textReceive.text = "資料如下"
        baseApplication.searchResult?.apply {
            baseApplication.mClient.read(address,
                    baseApplication.profile.services[0].uuid,
                    baseApplication.profile.services[0].characters[0].uuid) { code, data ->
                if (code == REQUEST_SUCCESS) {
                    textReceive.append("\n")
                    textReceive.append(data.toString())
                    Log.e("data", data.toString())
                }else {
                    textReceive.append("\n")
                    textReceive.append("接收失敗")
                }
            }
        }
    }
}
