package com.moisha.snek.glactivities

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.database.model.Level
import com.moisha.snek.game.Game
import com.moisha.snek.game.State
import com.moisha.snek.global.App
import com.moisha.snek.graphics.surfaces.GameSurface
import java.util.*
import kotlin.concurrent.timerTask

class GameActivity : AppCompatActivity() {

    private lateinit var mGLView: GameSurface
    private val gson: Gson = Gson()

    private lateinit var game: Game
    private val timer: Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLView = GameSurface(
            this@GameActivity
        )

        setContentView(mGLView)
        mGLView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        if (savedInstanceState?.containsKey("state") ?: false && intent.hasExtra("level")) {
            //if recreated from existing game

            for (i in 0..1000) {
                println(i)
            }
            val state: State = savedInstanceState?.getSerializable("state") as State
            val uId: Int = App.getUser()
            val level: Level = gson.fromJson(intent.getStringExtra("level"), Level::class.java)
            game = Game(level, uId, state)

        } else if (intent.hasExtra("level")) { // if called with level to play

            val uId: Int = App.getUser()
            val level: Level = gson.fromJson(intent.getStringExtra("level"), Level::class.java)
            game = Game(level, uId)

        } else { //if no ways to initialize game logic
            finish()
        }

        if (::game.isInitialized) {
            startGame()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (::game.isInitialized) {
            outState?.putSerializable("state", game.getState())
        }
    }

    fun action(coords: IntArray) {
        if (coords[0] == -1) {
            when (coords[1]) {
                2 -> {
                    game.setDirection(
                        Game.DIRECTOPN_LEFT
                    )
                }
                3 -> {
                    game.setDirection(
                        Game.DIRECTION_DOWN
                    )
                }
                4 -> {
                    game.setDirection(
                        Game.DIRECTION_UP
                    )
                }
                5 -> {
                    game.setDirection(
                        Game.DIRECTION_RIGHT
                    )
                }
            }
        }
    }

    private fun startGame() {
        if (::game.isInitialized) {
            timer.scheduleAtFixedRate(
                timerTask {
                    if (game.move()) {
                        mGLView.requestRender()
                    } else {
                        finish()
                    }
                },
                resources.getInteger(R.integer.move_time).toLong(),
                resources.getInteger(R.integer.move_time).toLong()
            )
        }
    }

    val getField = fun(): Array<IntArray> {
        return if (::game.isInitialized)
            game.getField() else arrayOf(intArrayOf(0))
    }
}
