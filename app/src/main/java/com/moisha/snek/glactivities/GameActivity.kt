package com.moisha.snek.glactivities

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.database.model.Level
import com.moisha.snek.game.objects.Game
import com.moisha.snek.graphics.surfaces.GameSurface

class GameActivity : AppCompatActivity() {

    private lateinit var mGLView: GameSurface
    private val gson: Gson = Gson()

    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLView = GameSurface(
            this@GameActivity
        )

        setContentView(mGLView)
        mGLView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        if (intent.hasExtra("level")) { // if called with level to play

             val level: Level = gson.fromJson(intent.getStringExtra("level"), Level::class.java)

         } else { // if no level

             finish()

         }

         setContentView(mGLView)
        mGLView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
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
}
