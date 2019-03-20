package com.moisha.snek.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
class Level constructor(listData: List<List<IntArray>>) {

    val gson = Gson()

    private var listData: List<List<IntArray>> = listData
    private var jsonData: String =
        gson.toJson(listData, object : TypeToken<List<List<IntArray>>>() {}.type)

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "data")
    var data: String? = jsonData

    fun getSize(): IntArray {
        return listData.first().first()
    }

    fun getBarriers(): List<IntArray> {
        return listData.get(1)
    }

    fun getSnek(): List<IntArray> {
        return listData.get(2)
    }
}