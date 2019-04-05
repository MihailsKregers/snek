package com.moisha.snek.graphics.surfaces

import android.content.Context
import android.opengl.GLSurfaceView
import com.moisha.snek.database.model.Level
import com.moisha.snek.game.GameHandle
import com.moisha.snek.global.App
import com.moisha.snek.graphics.GLRenderer

class GameSurface(context: Context, level: Level) : GLSurfaceView(context) {

    private var xOffset: Int = 0
    private var yOffset: Int = 0

    private val mRenderer: GLRenderer
    private lateinit var game: GameHandle

    init {

        setEGLContextClientVersion(2) //OpenGL ES 2.0

        mRenderer = GLRenderer()

        setRenderer(mRenderer)

        queueEvent {
            while (true) {
                if (width == 0 && height == 0) continue else {

                    val uId = App.getUser()
                    game = GameHandle(level, uId, width, height)

                    setRedraw()

                    break
                }
            }

            //setting offsets and menu after editor initialized
            val onScrLoc: IntArray = intArrayOf(0, 0)
            getLocationOnScreen(onScrLoc)
            xOffset = onScrLoc[0]
            yOffset = onScrLoc[1]

            mRenderer.menu = game.getMenuDrawData()

            requestRender()
        }

    }

    private fun setRedraw() {

        queueEvent {

            val drawData: Array<List<FloatArray>> = game.getRedrawData()

            mRenderer.sq_coords = drawData[0]
            mRenderer.sq_colors = drawData[1]

            requestRender()

        }

    }
}