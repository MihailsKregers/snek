package com.moisha.snek.graphics

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.moisha.snek.graphics.shapes.Square
import com.moisha.snek.graphics.shapes.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer : GLSurfaceView.Renderer {
    private lateinit var square: Square
    private lateinit var triangle: Triangle

    var sq_coords: List<FloatArray> = listOf()
    var sq_colors: List<FloatArray> = listOf()
    var tr_coords: List<FloatArray> = listOf()
    var tr_colors: List<FloatArray> = listOf()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.7f, 0.7f, 0.7f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }

        square = Square(mProgram)
        triangle = Triangle(mProgram)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {

        //draw all sent squares
        for (i in 0..sq_coords.lastIndex) {
            square.draw(sq_coords.get(i), sq_colors.get(i))
        }

        //draw all sent triangles
        for (i in 0..tr_coords.lastIndex) {
            triangle.draw(tr_coords.get(i), tr_colors.get(i))
        }
    }

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    private var mProgram: Int = 0

    fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

}