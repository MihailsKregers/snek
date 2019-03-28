package com.moisha.snek.glactivities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.database.model.Level
import com.moisha.snek.graphics.surfaces.EditorSurface

class EditorActivity : AppCompatActivity() {

    private lateinit var mGLView: EditorSurface
    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        if (intent.hasExtra("level") && intent.hasExtra("x") && intent.hasExtra("y")) {
            println("ALLEXTRAS\n\n\n\n\n")
            val level: Level = gson.fromJson("level", Level::class.java)
            mGLView = EditorSurface(
                this,
                level,
                intent.getIntExtra("x", R.integer.min_level_width),
                intent.getIntExtra("y", R.integer.min_level_height)
            )
        } else if (intent.hasExtra("level")) {
            println("LEVELEXTRA\n\n\n\n\n")
            val level: Level = gson.fromJson("level", Level::class.java)
            mGLView = EditorSurface(
                this,
                level
            )
        } else {
            println("NOEXTRA\n\n\n\n\n")
            mGLView = EditorSurface(
                this,
                intent.getIntExtra("x", 6),
                intent.getIntExtra("y", 4)
            )
        }
        //mGLView = EditorSurface(this, 20, 12)
        setContentView(mGLView)
    }
}
