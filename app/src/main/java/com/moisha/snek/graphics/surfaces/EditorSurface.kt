package com.moisha.snek.graphics.surfaces

import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.activities.SetLevelNameActivity
import com.moisha.snek.activities.SetSizeActivity
import com.moisha.snek.database.model.Level
import com.moisha.snek.editor.EditorHandle
import com.moisha.snek.glactivities.EditorActivity
import com.moisha.snek.global.App
import com.moisha.snek.graphics.GLRenderer
import kotlinx.android.synthetic.main.activity_set_level_name.view.*

class EditorSurface(context: Context, levelX: Int, levelY: Int) : GLSurfaceView(context) {

    private var yOffset: Int = 0

    private val mRenderer: GLRenderer
    private lateinit var editor: EditorHandle

    private val gson: Gson = Gson()

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

            mRenderer.menu = editor.getMenuDrawData()

            //..and field
            val drawData: Array<List<FloatArray>> = editor.getRedrawData()

            mRenderer.sq_coords = drawData[0]
            mRenderer.sq_colors = drawData[1]

            requestRender()
        }

    }

    constructor(context: Context, level: Level) : this(context, level.size[0], level.size[1]) {
        queueEvent {
            while (true) {
                if (width == 0 && height == 0) continue else {
                    editor = EditorHandle(level, width, height)

                    setRedraw()

                    break
                }
            }

            requestRender()
        }
    }

    constructor(context: Context, level: Level, x: Int, y: Int) : this(context, x, y) {
        queueEvent {
            while (true) {
                if (width == 0 && height == 0) continue else {
                    editor = EditorHandle(level, x, y, width, height)

                    setRedraw()

                    break
                }
            }

            requestRender()
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
                    val uId = App.getUser()
                    changeSize(
                        editor.getLevel(uId)
                    )
                }
                6 -> {
                    val uId = App.getUser()
                    saveLevel(
                        editor.getLevel(uId)
                    )
                }
                0 -> {
                    setRedraw()

                    requestRender()
                }
            }
        }
    }

    fun getLevel(): Level {

        val uId: Int = App.getUser()

        return editor.getLevel(uId)
    }

    private fun saveLevel(level: Level) {

        if (level.name.equals(context.getString(R.string.empty_level_name))) {

            val getNameIntent: Intent = Intent(
                context,
                SetLevelNameActivity::class.java
            )

            (context as EditorActivity).startActivityForResult(getNameIntent, EditorActivity.GET_NAME_REQUEST)

        } else {
            //
        }

        return

    }

    private fun changeSize(level: Level) {

        val changeSizeIntent: Intent = Intent(
            context,
            SetSizeActivity::class.java
        )

        changeSizeIntent.putExtra("level", gson.toJson(level, Level::class.java))

        (context as EditorActivity).startActivity(changeSizeIntent)

        return

    }

    private fun setRedraw() {

        val drawData: Array<List<FloatArray>> = editor.getRedrawData()

        mRenderer.sq_coords = drawData[0]
        mRenderer.sq_colors = drawData[1]

    }
}