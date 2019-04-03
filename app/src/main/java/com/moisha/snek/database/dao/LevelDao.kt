package com.moisha.snek.database.dao

import android.arch.persistence.room.*
import com.moisha.snek.database.model.Level

@Dao
interface LevelDao {
    @get:Query("SELECT * FROM levels")
    val all: List<Level>

    @Query("SELECT * FROM levels WHERE id = :levelId")
    fun getById(levelId: Int): Level

    @Insert
    fun insert(level: Level)

    @Query("SELECT COUNT(id) FROM levels WHERE name = :name")
    fun nameUsed(name: String): Int

    @Query("SELECT id FROM levels WHERE name = :name")
    fun getIdByName(name: String): Int

    @Update
    fun updateLevels(vararg levels: Level)

    @Delete
    fun delete(level: Level)
}