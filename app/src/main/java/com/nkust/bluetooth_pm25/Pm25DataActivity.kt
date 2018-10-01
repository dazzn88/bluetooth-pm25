package com.nkust.bluetooth_pm25

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.nkust.bluetooth_pm25.base.BaseActivity
import com.nkust.bluetooth_pm25.callback.FieldDataCallback
import com.nkust.bluetooth_pm25.libs.Helper
import com.nkust.bluetooth_pm25.libs.Utils
import com.nkust.bluetooth_pm25.models.ChannelFieldData
import com.nkust.bluetooth_pm25.models.Feed
import kotlinx.android.synthetic.main.activity_pm25_data.*
import java.io.IOException
import java.util.*

class Pm25DataActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pm25_data)
        setView()
    }

    private var selectIndex: Int = 0

    private val fieldDataCallback = object : FieldDataCallback() {
        override fun onFailure(e: IOException) {
            e.printStackTrace()
            Toast.makeText(this@Pm25DataActivity, "網路錯誤", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        }

        override fun onSuccess(channelFieldData: ChannelFieldData) {
            setData(channelFieldData)
            progressBar.visibility = View.GONE
        }
    }

    private fun setView() {
        buttonBackHome.setOnClickListener { finish() }
        textSelectTimeRange.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("請選擇時間區間")
                    .setSingleChoiceItems(R.array.time_range_type, selectIndex
                    ) { dialog, which ->
                        run {
                            progressBar.visibility = View.VISIBLE
                            selectIndex = which
                            textSelectTimeRange.text = resources.getStringArray(R.array.time_range_type)[selectIndex]
                            val average = when (selectIndex) {
                                0 -> 0
                                1 -> 1
                                2 -> 60
                                else -> 1
                            }
                            Helper.getFieldData(this, "1", average, fieldDataCallback)
                            dialog.dismiss()
                        }
                    }.show()
        }
        //設定圖標資訊
        val description = Description()
        description.text = "PM2.5"
        combinedChart.description = description
        combinedChart.animateXY(2000, 2000)
        combinedChart.xAxis.setValueFormatter { value, axis -> Utils.convertToTime(value.toLong()) }
        /*val calendar = Calendar.getInstance()
        calendar.set(2018, 8, 28, 0, 0, 0)*/
        Helper.getFieldData(this, "1", 1, fieldDataCallback)
    }

    private fun setData(channelFieldData: ChannelFieldData) {
        combinedChart.data = getBarData(channelFieldData.feeds)
        //lineChart.barData.barWidth =(0.25f)
        combinedChart.notifyDataSetChanged()
        combinedChart.invalidate() // refresh
    }

    @SuppressLint("DefaultLocale")
    private fun getBarData(dataObjects: List<Feed>): CombinedData {
        val dataSet = LineDataSet(getLineEntry(dataObjects), "")
        //dataSet.setColors(intArrayOf(R.color.blue_dark, R.color.purple_default), mActivity)
        dataSet.lineWidth = 2.5f
        dataSet.setDrawCircles(true)
        dataSet.setDrawCircleHole(false)
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.setDrawValues(false)
        //dataSet.setCircleColor(ContextCompat.getColor(mActivity, R.color.red_500))
        val combinedData = CombinedData()
        combinedData.setData(LineData(dataSet))
        combinedData.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->
            String.format("%,d %s", if (value > 1000f) (value / 1000f).toInt() else value.toInt(), if (value > 1000f) "k" else "")
        }
        //combinedData.setData(new LineData(dataSetB));
        return combinedData
    }

    private fun getLineEntry(dataObjects: List<Feed>): List<Entry> {
        val entries = ArrayList<Entry>()
        for (data in dataObjects) {
            data.field1?.apply {
                entries.add(Entry(Utils.convertToDate(data.createdAt).time.toFloat(), data.field1.toFloat()))
            }
        }
        return entries
    }
}
