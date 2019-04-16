package com.moisha.snek.activities

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.moisha.snek.R
import com.moisha.snek.App
import com.moisha.snek.activities.gl.EditorActivity

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
                        startActivity(Intent(this@MainActivity, StartGameActivity::class.java))
                    }
                    1 -> {
                        //Level manager
                        startActivity(Intent(this@MainActivity, EditLevelActivity::class.java))
                    }
                    2 -> {
                        //Highscores
                        startActivity(Intent(this@MainActivity, HighscoreLevelActivity::class.java))
                    }
                    3 -> {
                        //Create level
                        startActivity(Intent(this@MainActivity, EditorActivity::class.java))
                    }
                    4 -> {
                        //Change user
                        changeUser()
                    }
                    5 -> {
                        //Exit game
                        ActivityCompat.finishAffinity(this@MainActivity)
                    }
                }
            }
        }

    }

    private fun changeUser() {
        App.logOff()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
