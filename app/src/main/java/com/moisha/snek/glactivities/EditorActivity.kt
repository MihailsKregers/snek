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
        val GET_SIZE_REQUEST: Int = 2
    }


    private lateinit var mGLView: EditorSurface
    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        // if called with level data and new level size for resizing
        if (intent.hasExtra("level")) { // if called with level for editing

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

                    mGLView.saveLevel(
                        data?.getStringExtra("name") ?: resources.getString(R.string.empty_level_name)
                    )

                }
            } else if (requestCode == GET_SIZE_REQUEST) {
                println("YES")
                println("YES")
                println("YES")
                println("YdfdfdfES")
                println("YES")
                println("YES")
                println("YES")
                println("YES")
                if (data?.hasExtra("x") ?: false && data?.hasExtra("y") ?: false) {

                    println("YES")
                    println("YES")
                    println("YES")
                    println("YES")
                    println("YES")
                    println("YES")
                    println("YES")
                    println("YES")
                    mGLView.resizeLevel(
                        data?.getIntExtra(
                            "x",
                            resources.getInteger(R.integer.min_level_width)
                        ) ?: resources.getInteger(R.integer.min_level_width),
                        data?.getIntExtra(
                            "y",
                            resources.getInteger(R.integer.min_level_height)
                        ) ?: resources.getInteger(R.integer.min_level_height)
                    )

                }
            }
        }
    }

}
