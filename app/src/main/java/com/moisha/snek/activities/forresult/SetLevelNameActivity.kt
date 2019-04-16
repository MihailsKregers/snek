package com.moisha.snek.activities.forresult

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.moisha.snek.R
import com.moisha.snek.database.DatabaseInstance
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

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
        var used: Int

        doAsync {

            used = DatabaseInstance.getInstance(this@SetLevelNameActivity).levelDao().nameUsed(name)

            uiThread {

                if (name.equals(resources.getString(R.string.empty_string))) {

                    findViewById<TextView>(R.id.level_name_error).setText(R.string.name_empty_error)

                } else if (used > 0) {

                    findViewById<TextView>(R.id.level_name_error).setText(R.string.name_error)

                } else {

                    val result: Intent = Intent()

                    result.putExtra("name", name)

                    setResult(Activity.RESULT_OK, result)

                    finish()

                }
            }
        }
    }
}
