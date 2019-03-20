package com.moisha.snek.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.moisha.snek.database.model.Player


@Entity(
    tableName = "highscores",
    foreignKeys = arrayOf(
        ForeignKey(entity = Player::class, parentColumns = arrayOf("name"), childColumns = arrayOf("pName")),
        ForeignKey(entity = Level::class, parentColumns = arrayOf("id"), childColumns = arrayOf("levelId"))
    )
)
class Highcore(pName: String, levelId: Int, score: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "pName")
    var pName: String = pName

    @ColumnInfo(name = "levelId")
    var levelId: Int = levelId

    @ColumnInfo(name = "score")
    var score: Int = score
}