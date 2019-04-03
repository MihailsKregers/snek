package com.moisha.snek.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.moisha.snek.R
import com.moisha.snek.global.App

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        if (!App.isAuth()) {
            changeUser()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.mainMenu)

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (id.toInt()) {
                    0 -> {
                        //Start game
                    }
                    1 -> {
                        //Highscores
                        startActivity(Intent(this@MainActivity, SetLevelNameActivity::class.java))
                    }
                    2 -> {
                        startActivity(Intent(this@MainActivity, EditLevelActivity::class.java))
                    }
                    3 -> {
                        //Delete level
                    }
                    4 -> {
                        startActivity(Intent(this@MainActivity, SetSizeActivity::class.java))
                    }
                    5 -> {
                        //Edit level
                    }
                    6 -> changeUser()
                    7 -> finishAffinity()
                }
            }
        }

    }

    private fun changeUser() {
        App.logOff()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
