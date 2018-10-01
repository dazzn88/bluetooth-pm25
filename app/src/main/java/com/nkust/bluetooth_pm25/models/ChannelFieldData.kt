package com.nkust.bluetooth_pm25.models

import com.google.gson.annotations.SerializedName


data class ChannelFieldData(
        @SerializedName("channel") val channel: Channel,
        @SerializedName("feeds") val feeds: List<Feed>

) {
    override fun toString(): String {
        return "ChannelFieldData(channel=$channel, feeds=$feeds)"
    }
}

data class Channel(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("latitude") val latitude: String,
        @SerializedName("longitude") val longitude: String,
        @SerializedName("field1") val field1: String,
        @SerializedName("field2") val field2: String,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("last_entry_id") val lastEntryId: Int


) {
    override fun toString(): String {
        return "Channel(id=$id, name='$name', latitude='$latitude', longitude='$longitude', field1='$field1', field2='$field2', createdAt='$createdAt', updatedAt='$updatedAt', lastEntryId=$lastEntryId)"
    }
}

data class Feed(
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("entry_id") val entryId: Int,
        @SerializedName("field1") val field1: String?
) {
    override fun toString(): String {
        return "Feed(createdAt='$createdAt', entryId=$entryId, field1='$field1')"
    }
}