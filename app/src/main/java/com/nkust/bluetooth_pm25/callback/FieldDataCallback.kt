package com.nkust.bluetooth_pm25.callback

import com.nkust.bluetooth_pm25.models.ChannelFieldData
import java.io.IOException

/**
 * Created by Ray on 2017/8/11.
 */

abstract class FieldDataCallback {

    abstract fun onFailure(e: IOException)

    abstract fun onSuccess(channelFieldData: ChannelFieldData)
}
