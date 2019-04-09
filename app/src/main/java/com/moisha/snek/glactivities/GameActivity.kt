package com.moisha.snek.glactivities

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.database.model.Level
import com.moisha.snek.graphics.surfaces.GameSurface

class GameActivity : AppCompatActivity() {

    private lateinit var mGLView: GameSurface
    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        /* if (intent.hasExtra("level")) { // if called with level to play

             val level: Level = gson.fromJson(intent.getStringExtra("level"), Level::class.java)
             mGLView = GameSurface(
                 this@GameActivity,
                 level
             )

         } else { // if no level

             finish()

         }

         setContentView(mGLView)
         mGLView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY*/
    }
}
