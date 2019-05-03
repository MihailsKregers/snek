package com.moisha.snek.activities.forresult

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.database.model.Level

class SetSizeActivity : AppCompatActivity() {

    private val gson: Gson = Gson()
    private lateinit var level: Level

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_size)

        if (intent.hasExtra("x") && intent.hasExtra("y")) {

            findViewById<EditText>(R.id.set_size_x).setText(
                intent.getIntExtra("x", resources.getInteger(R.integer.min_level_width)).toString()
            )
            findViewById<EditText>(R.id.set_size_y).setText(
                intent.getIntExtra("y", resources.getInteger(R.integer.min_level_height)).toString()
            )

        }
    }

    fun returnResult(view: View) {
        val xText: String = findViewById<EditText>(R.id.set_size_x).text.toString()
        val yText: String = findViewById<EditText>(R.id.set_size_y).text.toString()

        if (xText.length < 1 || yText.length < 1) {
            error()
        } else {

            val x: Int = xText.toInt()
            val y: Int = yText.toInt()

            var hasErrors: Boolean = false

            if (x < resources.getInteger(R.integer.min_level_width)
                || x > resources.getInteger(R.integer.max_level_width)
            ) {
                findViewById<TextView>(R.id.x_set_error).setText(R.string.x_error)
                hasErrors = true
            } else {
                findViewById<TextView>(R.id.x_set_error).setText(R.string.empty_string)
            }

            if (y < resources.getInteger(R.integer.min_level_height)
                || y > resources.getInteger(R.integer.max_level_height)
            ) {
                findViewById<TextView>(R.id.y_set_error).setText(R.string.y_error)
                hasErrors = true
            } else {
                findViewById<TextView>(R.id.y_set_error).setText(R.string.empty_string)
            }

            if (!hasErrors) {

                val result: Intent = Intent()

                result.putExtra("x", x)
                result.putExtra("y", y)

                setResult(Activity.RESULT_OK, result)

                finish()

            }

        }

    }

    private fun error() {
        this.runOnUiThread {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@SetSizeActivity)
                .setMessage(R.string.wsize_body)
                .setTitle(R.string.wsize_title)
                .setNeutralButton(
                    R.string.ok
                ) { _: DialogInterface, _: Int -> }
            val alert: AlertDialog = builder.create()

            alert.show()
        }
    }
}
