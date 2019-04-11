package com.moisha.snek.database.model

import android.arch.persistence.room.*

@Entity(
    tableName = "playerLevel",
    foreignKeys = arrayOf(
        ForeignKey(entity = Player::class, parentColumns = arrayOf("id"), childColumns = arrayOf("uId")),
        ForeignKey(entity = Level::class, parentColumns = arrayOf("id"), childColumns = arrayOf("levelId"))
    ),
    indices = arrayOf(
        Index(value = arrayOf("id", "uId", "levelId")),
        Index(value = arrayOf("uId", "levelId"), unique = true)
    )
)
class PlayerLevel(uId: Int, levelId: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "uId")
    var uId: Int = uId

    @ColumnInfo(name = "levelId")
    var levelId: Int = levelId
}