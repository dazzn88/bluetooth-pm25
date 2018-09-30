package com.nkust.bluetooth_pm25.libs

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ray on 2017/2/24.
 */

object Utils {

    fun convertToDate(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        return try {
            simpleDateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            simpleDateFormat.format(Date())
        }
    }

    fun convertToDate(date: String): Date {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        return try {
            simpleDateFormat.parse(date)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    fun convertToTime(toLong: Long): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.TAIWAN)
        return try {
            simpleDateFormat.format(Date(toLong))
        } catch (e: Exception) {
            e.printStackTrace()
            simpleDateFormat.format(Date())
        }
    }
}
