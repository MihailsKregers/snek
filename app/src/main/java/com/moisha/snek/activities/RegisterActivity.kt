package com.moisha.snek.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.View
import com.moisha.snek.R
import com.moisha.snek.database.DatabaseInstance
import com.moisha.snek.database.model.Player
import com.moisha.snek.global.App
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun registerPlayer(view: View) {
        val name = findViewById<TextInputEditText>(R.id.register_name).text.toString()

        doAsync {
            val dao = DatabaseInstance.getInstance(this@RegisterActivity).playerDao()

            if (dao.nameUsed(name) == 1) {
                //
            } else {
                dao.insert(Player(name))
                val uId: Int = dao.getIdByName(name)
                uiThread {
                    App.setUser(uId)
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                }
            }
        }
    }

}
