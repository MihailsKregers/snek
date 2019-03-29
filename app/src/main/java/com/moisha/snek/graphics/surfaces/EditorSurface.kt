package com.moisha.snek.graphics.surfaces

import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.moisha.snek.R
import com.moisha.snek.activities.SetLevelNameActivity
import com.moisha.snek.database.model.Level
import com.moisha.snek.editor.EditorHandle
import com.moisha.snek.glactivities.EditorActivity
import com.moisha.snek.global.App
import com.moisha.snek.graphics.GLRenderer

class EditorSurface(context: Context, levelX: Int, levelY: Int) : GLSurfaceView(context) {

    private var yOffset: Int = 0

    private val mRenderer: GLRenderer
    private lateinit var editor: EditorHandle

    init {
        setEGLContextClientVersion(2) //OpenGL ES 2.0

        mRenderer = GLRenderer()

        setRenderer(mRenderer)

        queueEvent {

            //init editor, when viewport size data is set
            while (true) {
                if (width == 0 || height == 0) continue else {
                    editor = EditorHandle(levelX, levelY, width, height)
                    val onScrLoc: IntArray = intArrayOf(0, 0)
                    getLocationOnScreen(onScrLoc)
                    yOffset = onScrLoc[1]
                    break
                }
            }

            //when editor initialized draw menu
            val menu: Array<List<FloatArray>> = editor.getMenuDrawData()

            mRenderer.sq_coords = menu[0]
            mRenderer.sq_colors = menu[1]
            mRenderer.tr_coords = menu[2]
            mRenderer.tr_colors = menu[3]

            //..and field
            val drawData: Array<List<FloatArray>> = editor.getRedrawData()

            mRenderer.sq_coords = listOf(mRenderer.sq_coords, drawData[0]).flatten()
            mRenderer.sq_colors = listOf(mRenderer.sq_colors, drawData[1]).flatten()

            requestRender()
        }

    }

    constructor(context: Context, level: Level) : this(context, level.size[0], level.size[1]) {
        queueEvent {
            while (true) {
                if (width == 0 && height == 0) continue else {
                    editor = EditorHandle(level, width, height)
                    break
                }
            }
        }
    }

    constructor(context: Context, level: Level, x: Int, y: Int) : this(context, x, y) {
        queueEvent {
            while (true) {
                if (width == 0 && height == 0) {
                    editor = EditorHandle(level, x, y, width, height)
                    break
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            queueEvent(object : Runnable {
                override fun run() {
                    action(event.x, event.y - yOffset)
                }
            })
        }

        return true
    }

    fun action(x: Float, y: Float) {
        queueEvent {
            when (editor.reactOnClick(x.toInt(), y.toInt())) {
                5 -> {
                }
                6 -> {
                    val uId = App.getUser()
                    saveLevel(
                        editor.getLevel(uId)
                    )
                }
                0 -> {
                    val drawData: Array<List<FloatArray>> = editor.getRedrawData()

                    mRenderer.sq_coords = drawData[0]
                    mRenderer.sq_colors = drawData[1]

                    requestRender()
                }
            }
        }
    }

    private fun saveLevel(level: Level) {
        if (level.name.equals(R.string.empty_level_name)) {
            val saveLevelIntent: Intent = Intent(
                context, SetLevelNameActivity::class.java
            )
            (context as EditorActivity).startActivity(saveLevelIntent)
        }
        return
    }
}