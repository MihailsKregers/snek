package com.moisha.snek.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.moisha.snek.database.dao.HighscoreDao
import com.moisha.snek.database.dao.LevelDao
import com.moisha.snek.database.dao.PlayerDao
import com.moisha.snek.database.model.Highcore
import com.moisha.snek.database.model.Level
import com.moisha.snek.database.model.Player

@Database(version = 1, entities = [(Level::class), (Player::class), (Highcore::class)], exportSchema = false)
abstract class DatabaseInstance : RoomDatabase() {

    abstract fun levelDao(): LevelDao

    abstract fun playerDao(): PlayerDao

    abstract fun highscoreDao(): HighscoreDao

    companion object {

        private var dbInst: DatabaseInstance? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseInstance {
            if (dbInst == null) {
                dbInst = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseInstance::class.java,
                    "SnekData"
                ).build()
            }
            return dbInst!!
        }
    }
}