package com.moisha.snek.database.converter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LevelConverter {

    private val gson = Gson()

    @TypeConverter
    fun listToJson(value: List<IntArray>?): String? {
        return gson.toJson(value, object : TypeToken<List<IntArray>>() {}.type)
    }

    @TypeConverter
    fun jsonToList(value: String?): List<IntArray>? {
        return gson.fromJson(value, object : TypeToken<List<IntArray>>() {}.type)
    }

    @TypeConverter
    fun intArrayToJson(value: IntArray?): String? {
        return gson.toJson(value, object : TypeToken<IntArray>() {}.type)
    }

    @TypeConverter
    fun jsonToIntArray(value: String?): IntArray? {
        return gson.fromJson(value, object : TypeToken<IntArray>() {}.type)
    }
}