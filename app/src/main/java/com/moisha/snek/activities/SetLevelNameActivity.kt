package com.moisha.snek.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.moisha.snek.R
import com.moisha.snek.glactivities.EditorActivity

class SetLevelNameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_level_name)
    }

    fun backToEditor(view: View) {
        this.finish()
    }

    fun sendResult(view: View) {

        val name = findViewById<EditText>(R.id.level_name).text.toString()

        if (name.equals(resources.getString(R.string.empty_string))) {

            findViewById<TextView>(R.id.level_name_error).setText(R.string.name_empty_error)

        } else if (false) {
            //
        } else {

            val result: Intent = Intent()

            result.putExtra("name", name)

            setResult(Activity.RESULT_OK, result)

            finish()

        }
    }
}
