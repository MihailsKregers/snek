package com.moisha.snek.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import com.moisha.snek.database.model.PlayerLevel

@Dao
interface PlayerLevelDao {
    @Insert
    fun insert(value: PlayerLevel)

    @Delete
    fun delete(value: PlayerLevel)
}