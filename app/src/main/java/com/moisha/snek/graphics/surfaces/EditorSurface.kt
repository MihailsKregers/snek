package com.moisha.snek.graphics.surfaces

import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.activities.SetLevelNameActivity
import com.moisha.snek.activities.SetSizeActivity
import com.moisha.snek.database.DatabaseInstance
import com.moisha.snek.database.dao.LevelDao
import com.moisha.snek.database.model.Level
import com.moisha.snek.editor.EditorHandle
import com.moisha.snek.glactivities.EditorActivity
import com.moisha.snek.global.App
import com.moisha.snek.graphics.GLRenderer
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

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
            setRedraw()
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

        when (editor.reactOnClick(x.toInt(), y.toInt())) {
            5 -> changeSize()
            6 -> saveLevel()
            0 -> setRedraw()
        }

    }

    fun resizeLevel(x: Int, y: Int) {

        val uId: Int = App.getUser()
        val level: Level = editor.getLevel(uId)

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

    fun saveLevel(name: String = resources.getString(R.string.empty_level_name)) {

        if (!name.equals(resources.getString(R.string.empty_level_name))) {

            editor.setName(name)

        }

        if (editor.getName().equals(resources.getString(R.string.empty_level_name))) {

            val getNameIntent: Intent = Intent(
                context,
                SetLevelNameActivity::class.java
            )

            (context as EditorActivity).startActivityForResult(getNameIntent, EditorActivity.GET_NAME_REQUEST)

        } else {

            val uId = App.getUser()
            val level: Level = editor.getLevel(uId)

            doAsync {

                val lvldao: LevelDao = DatabaseInstance.getInstance(context).levelDao()

                if (lvldao.nameUsed(editor.getName()) > 0) {

                    level.id = lvldao.getIdByName(level.name)
                    lvldao.updateLevels(level)

                } else {

                    lvldao.insert(level)

                }

                uiThread {

                    (context as EditorActivity).finish()

                }
            }
        }

        return

    }

    private fun changeSize() {

        val getSizeIntent: Intent = Intent(
            context,
            SetSizeActivity::class.java
        )

        getSizeIntent.putExtra("x", editor.getX())
        getSizeIntent.putExtra("y", editor.getY())

        (context as EditorActivity).startActivityForResult(getSizeIntent, EditorActivity.GET_SIZE_REQUEST)

        return

    }

    private fun setRedraw() {

        queueEvent {

            val drawData: Array<List<FloatArray>> = editor.getRedrawData()

            mRenderer.sq_coords = drawData[0]
            mRenderer.sq_colors = drawData[1]

            requestRender()

        }

    }
}