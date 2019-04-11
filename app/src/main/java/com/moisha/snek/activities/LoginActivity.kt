package com.moisha.snek.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.moisha.snek.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val listView: ListView = findViewById(R.id.loginMenu)

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (id.toInt()) {
                    0 -> startActivity(Intent(this@LoginActivity, SelectUserActivity::class.java))
                    1 -> startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
            }
        }
    }
}
