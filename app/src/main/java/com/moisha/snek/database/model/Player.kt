package com.moisha.snek.database.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "players",
    indices = arrayOf(
        Index(value = arrayOf("name"))
    )
)
class Player(name: String) {
    @PrimaryKey
    var name: String = name
}