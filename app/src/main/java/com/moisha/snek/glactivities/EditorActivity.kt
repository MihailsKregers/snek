package com.moisha.snek.glactivities

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.activities.SetLevelNameActivity
import com.moisha.snek.activities.SetSizeActivity
import com.moisha.snek.database.DatabaseInstance
import com.moisha.snek.database.dao.LevelDao
import com.moisha.snek.database.model.Level
import com.moisha.snek.editor.EditorField
import com.moisha.snek.global.App
import com.moisha.snek.graphics.surfaces.EditorSurface
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditorActivity : AppCompatActivity() {

    companion object {
        const val GET_NAME_REQUEST: Int = 1
        const val GET_SIZE_REQUEST: Int = 2
    }

    private lateinit var mGLView: EditorSurface
    private lateinit var editor: EditorField
    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLView = EditorSurface(
            this@EditorActivity
        )

        setContentView(mGLView)

        if (savedInstanceState?.containsKey("level") ?: false) { //if recreated from existed state

            val jsonEditorField: String = savedInstanceState?.getString("level")!!

            this.editor = gson.fromJson<EditorField>(
                jsonEditorField,
                EditorField::class.java
            )

        } else if (intent.hasExtra("level")) { // if called with level for editing

            val level: Level = gson.fromJson(intent.getStringExtra("level"), Level::class.java)
            editor = EditorField(level)


        } else { // if called for new level creation - ask size

            val intent = Intent(
                this@EditorActivity,
                SetSizeActivity::class.java
            )

            startActivityForResult(intent, EditorActivity.GET_SIZE_REQUEST)

        }

        if (::editor.isInitialized) { //if editor successfully initialized, draw contents after view is set

            mGLView.requestRender()

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (::editor.isInitialized) {
            outState?.putString(
                "level",
                gson.toJson(editor)
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_NAME_REQUEST) {
                if (data?.hasExtra("name") ?: false) {

                    saveLevel(
                        data?.getStringExtra("name") ?: resources.getString(R.string.empty_level_name)
                    )

                }
            } else if (requestCode == GET_SIZE_REQUEST) {

                if (data?.hasExtra("x") ?: false && data?.hasExtra("y") ?: false) {

                    val x: Int = data?.getIntExtra(
                        "x",
                        resources.getInteger(R.integer.min_level_width)
                    ) ?: resources.getInteger(R.integer.min_level_width)
                    val y: Int = data?.getIntExtra(
                        "y",
                        resources.getInteger(R.integer.min_level_height)
                    ) ?: resources.getInteger(R.integer.min_level_height)

                    if (::editor.isInitialized) {
                        editor.changeSize(x, y)
                    } else {
                        editor = EditorField(x, y)
                    }

                    mGLView.requestRender()
                }

            }
        }
    }

    fun error() {
        this.runOnUiThread {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.snek_size_error)
            builder.setTitle(R.string.short_snek_error)
            builder.setNeutralButton(
                R.string.ok,
                { dialogInterface: DialogInterface, i: Int -> }
            )
            val error: AlertDialog = builder.create()

            error.show()
        }
    }

    fun action(coords: IntArray) {

        if (coords[0] == -1) {
            when (coords[1]) {
                1 -> {
                    editor.setAction(
                        EditorField.ACTION_SET_SNEK
                    )
                }
                2 -> {
                    editor.setAction(
                        EditorField.ACTION_SET_BARRIER
                    )
                }
                3 -> {
                    editor.clearSnek()
                }
                4 -> {
                    editor.clearBarriers()
                }
                5 -> {
                    changeSize()
                }
                6 -> {
                    saveLevel()
                }
            }
        } else {
            editor.react(coords)
        }

        mGLView.requestRender()
    }

    val getField = fun(): Array<IntArray> {
        return if (::editor.isInitialized)
            editor.getField() else arrayOf(intArrayOf(0))
    }

    private fun changeSize() {

        val getSizeIntent = Intent(
            this@EditorActivity,
            SetSizeActivity::class.java
        )

        getSizeIntent.putExtra("x", editor.getX())
        getSizeIntent.putExtra("y", editor.getY())

        startActivityForResult(getSizeIntent, EditorActivity.GET_SIZE_REQUEST)

        return

    }

    private fun saveLevel(name: String = resources.getString(R.string.empty_level_name)) {
        if (editor.getSnekSize() - 1 < resources.getInteger(R.integer.min_snek_size)) {
            error()
            return
        }

        if (!name.equals(resources.getString(R.string.empty_level_name))) {

            editor.levelName = name

        }

        if (editor.levelName.equals(resources.getString(R.string.empty_level_name))) {

            val getNameIntent = Intent(
                this@EditorActivity,
                SetLevelNameActivity::class.java
            )

            startActivityForResult(getNameIntent, EditorActivity.GET_NAME_REQUEST)

        } else {

            val uId = App.getUser()
            val level: Level = editor.getLevel(uId)

            doAsync {

                val lvldao: LevelDao = DatabaseInstance.getInstance(this@EditorActivity).levelDao()

                if (lvldao.nameUsed(level.name) > 0) {

                    level.id = lvldao.getIdByName(level.name)
                    lvldao.updateLevels(level)

                } else {

                    lvldao.insert(level)

                }

                uiThread {

                    finish()

                }
            }
        }

        return

    }

}
