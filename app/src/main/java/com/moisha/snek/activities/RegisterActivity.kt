package com.moisha.snek.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.moisha.snek.R
import com.moisha.snek.database.DatabaseInstance
import com.moisha.snek.database.model.Player
import com.moisha.snek.App
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun registerPlayer(view: View) {
        val name = findViewById<EditText>(R.id.register_name).text.toString()

        if (name.equals(resources.getString(R.string.empty_string))) {
            findViewById<TextView>(R.id.register_error).setText(R.string.name_empty_error)
            return
        }

        doAsync {
            val dao = DatabaseInstance.getInstance(this@RegisterActivity).playerDao()

            if (dao.nameUsed(name) > 0) {
                uiThread {
                    findViewById<TextView>(R.id.register_error).setText(R.string.name_error)
                }
            } else {
                dao.insert(Player(name))
                val uId: Int = dao.getIdByName(name)
                uiThread {
                    App.setUser(uId)
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

}
