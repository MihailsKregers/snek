package com.moisha.snek.database.model

import android.arch.persistence.room.*

@Entity(
    tableName = "playerLevel",
    foreignKeys = arrayOf(
        ForeignKey(entity = Player::class, parentColumns = arrayOf("name"), childColumns = arrayOf("pName")),
        ForeignKey(entity = Level::class, parentColumns = arrayOf("id"), childColumns = arrayOf("levelId"))
    ),
    indices = arrayOf(
        Index(value = arrayOf("pName", "levelId"), unique = true)
    )
)
class PlayerLevel(pName: String, levelId: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "pName")
    var pName: String = pName

    @ColumnInfo(name = "levelId")
    var levelId: Int = levelId
}