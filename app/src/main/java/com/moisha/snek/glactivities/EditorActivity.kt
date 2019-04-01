package com.moisha.snek.glactivities

import android.app.Activity
import android.content.Intent
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.database.model.Level
import com.moisha.snek.graphics.surfaces.EditorSurface

class EditorActivity : AppCompatActivity() {

    companion object {
        val GET_NAME_REQUEST: Int = 1
    }


    private lateinit var mGLView: EditorSurface
    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        // if called with level data and new level size for resizing
        if (intent.hasExtra("level") && intent.hasExtra("x") && intent.hasExtra("y")) {

            val level: Level = gson.fromJson(intent.getStringExtra("level"), Level::class.java)
            mGLView = EditorSurface(
                this@EditorActivity,
                level,
                intent.getIntExtra("x", resources.getInteger(R.integer.min_level_width)),
                intent.getIntExtra("y", resources.getInteger(R.integer.min_level_height))
            )

        } else if (intent.hasExtra("level")) { // if called with level for editing

            val level: Level = gson.fromJson(intent.getStringExtra("level"), Level::class.java)
            mGLView = EditorSurface(
                this@EditorActivity,
                level
            )

        } else { // if called for new level creation

            mGLView = EditorSurface(
                this@EditorActivity,
                intent.getIntExtra("x", resources.getInteger(R.integer.min_level_width)),
                intent.getIntExtra("y", resources.getInteger(R.integer.min_level_height))
            )

        }

        setContentView(mGLView)
        mGLView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_NAME_REQUEST) {
                if (data?.hasExtra("name") ?: false) {
                    val level: Level = mGLView.getLevel()

                    level.name = data?.getStringExtra("name") ?: resources.getString(R.string.empty_level_name)
                    println(level.name)
                    println(level.name)
                    println(level.name)
                    println(level.name)
                    println(level.name)
                    println(level.name)
                }
            }
        }
    }

    fun saveLevel() {
        //
    }
}
