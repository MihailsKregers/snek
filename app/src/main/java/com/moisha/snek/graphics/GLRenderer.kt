package com.moisha.snek.graphics

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.moisha.snek.graphics.shapes.Square
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer : GLSurfaceView.Renderer {
    private lateinit var square: Square
    var coords: List<FloatArray> = listOf()
    var colors: List<FloatArray> = listOf()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.7f, 0.7f, 0.7f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        square = Square()
        square.draw(
            floatArrayOf(-0.1f, -0.1f, -0.1f, 0.1f, 0.1f, 0.1f, 0.1f, -0.1f),
            floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        for (i in 0..coords.lastIndex) {
            square.draw(coords.get(i), colors.get(i))
        }
    }

}