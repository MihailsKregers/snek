package com.moisha.snek.database.model

import android.arch.persistence.room.*
import com.moisha.snek.database.model.Player


@Entity(
    tableName = "highscores",
    foreignKeys = arrayOf(
        ForeignKey(entity = Player::class, parentColumns = arrayOf("id"), childColumns = arrayOf("uId")),
        ForeignKey(entity = Level::class, parentColumns = arrayOf("id"), childColumns = arrayOf("levelId"))
    ),
    indices = arrayOf(
        Index(value = arrayOf("id", "uId", "levelId", "score"))
    )
)
class Highcore(uId: Int, levelId: Int, score: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "uId")
    var uId: Int = uId

    @ColumnInfo(name = "levelId")
    var levelId: Int = levelId

    @ColumnInfo(name = "score")
    var score: Int = score
}