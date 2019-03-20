package com.moisha.snek.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "levels",
    foreignKeys = arrayOf(
        ForeignKey(entity = Player::class, parentColumns = arrayOf("name"), childColumns = arrayOf("pName"))
    )
)
class Level constructor(size: IntArray, barriers: List<IntArray>, snek: List<IntArray>, direction: Int, pName: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "size")
    var size: IntArray = size

    @ColumnInfo(name = "barriers")
    var barriers: List<IntArray> = barriers

    @ColumnInfo(name = "snek")
    var snek: List<IntArray> = snek

    @ColumnInfo(name = "direction")
    var direction: Int = direction

    @ColumnInfo(name = "pName")
    var pName: String = pName
}