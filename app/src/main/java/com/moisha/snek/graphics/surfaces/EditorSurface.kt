package com.moisha.snek.graphics.surfaces

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.moisha.snek.database.model.Level
import com.moisha.snek.editor.EditorHandle
import com.moisha.snek.graphics.GLRenderer

class EditorSurface(context: Context, levelX: Int, levelY: Int) : GLSurfaceView(context) {

    private val mRenderer: GLRenderer
    private lateinit var editor: EditorHandle

    init {
        setEGLContextClientVersion(2) //OpenGL ES 2.0

        mRenderer = GLRenderer()

        setRenderer(mRenderer)

        queueEvent {
            while (true) {
                if (width == 0 || height == 0) continue else {
                    editor = EditorHandle(levelX, levelY, width, height)
                    break
                }
            }
            val fld: Array<List<FloatArray>> = editor.getRedrawData()
            mRenderer.coords = fld[0]
            mRenderer.colors = fld[1]
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
                    action(event.x, event.y)
                    println(event.x)
                }
            })
        }

        queueEvent {
            mRenderer.coords = listOf(editor.getMenuField().get(0))
            mRenderer.colors = listOf(editor.getMenuField().get(1))

            requestRender()
        }
        println(width)
        println(height)
        return true
    }

    fun action(x: Float, y: Float) {
        //
        return
    }
}