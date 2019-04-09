package com.moisha.snek.graphics.surfaces

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.moisha.snek.glactivities.EditorActivity
import com.moisha.snek.graphics.GLRenderer

class EditorSurface constructor(context: Context) : GLSurfaceView(context) {

    private val mRenderer: GLRenderer
    private val editorActivity: EditorActivity = context as EditorActivity

    init {

        setEGLContextClientVersion(2) //OpenGL ES 2.0

        mRenderer = GLRenderer(2)

        setRenderer(mRenderer)

        queueEvent {
            while (true) {
                if (width > 0 && height > 0) {
                    while (!editorActivity.draw()) {
                        continue
                    }
                    break
                }
            }
            requestRender()
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            queueEvent(
                object : Runnable {
                    override fun run() {
                        val onScrLoc: IntArray = intArrayOf(0, 0)
                        getLocationOnScreen(onScrLoc)

                        editorActivity.action(
                            mRenderer.traceClick(
                                event.x.toInt() - onScrLoc[0],
                                event.y.toInt() - onScrLoc[1]
                            )
                        )
                    }
                }
            )
        }

        return true
    }

    fun redrawField(field: Array<IntArray>) {
        mRenderer.redrawField(field)
        requestRender()
    }

}