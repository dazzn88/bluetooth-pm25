package com.nkust.bluetooth_pm25.libs

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.nkust.bluetooth_pm25.callback.FieldDataCallback
import com.nkust.bluetooth_pm25.models.ChannelFieldData
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Ray on 2017/12/20.
 */

object Helper {
    private val MEDIA_TYPE_JSON = MediaType.parse("application/json")

    private const val HOST = "api.thingspeak.com"
    private const val HOST_LOCAL = "192.168.101.10"
    private const val CHANNEL_ID = "577971"

    private const val API_URL = "https://$HOST"

    private fun createOkHttpCall(url: String): Call {
        val mOkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build()
        val request = Request.Builder()
                .url(url)
                .build()
        return mOkHttpClient.newCall(request)
    }

    fun getFieldData(context: Context, fieldId: String, average: Int, callback: FieldDataCallback) {
        val url = "$API_URL/channels/$CHANNEL_ID/fields/$fieldId.json?average=$average"
        val call = createOkHttpCall(url)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                (context as Activity).runOnUiThread { callback.onFailure(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val fieldData = Gson().fromJson<ChannelFieldData>(str, ChannelFieldData::class.java)
                (context as Activity).runOnUiThread {
                    callback.onSuccess(fieldData)
                }
            }
        })
    }
}
