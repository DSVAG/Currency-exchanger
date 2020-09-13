package com.dsvag.currencyexchanger.data.utils

import androidx.room.TypeConverter
import com.dsvag.currencyexchanger.data.models.latest.Platform
import com.dsvag.currencyexchanger.data.models.latest.Quote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromPlatform(platform: Platform?): String {
        return if (platform != null) {
            Gson().toJson(platform).toString()
        } else {
            ""
        }
    }

    @TypeConverter
    fun toPlatform(string: String): Platform? {
        return Gson().fromJson(string, Platform::class.java)
    }

    @TypeConverter
    fun fromQuote(quote: Quote): String {
        return Gson().toJson(quote).toString()
    }

    @TypeConverter
    fun toQuote(string: String): Quote {
        return Gson().fromJson(string, Quote::class.java)
    }

    @TypeConverter
    fun fromTags(list: List<String>): String {
        return Gson().toJson(list).toString()
    }

    @TypeConverter
    fun toTags(string: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(string, type)
    }
}