package com.moisha.snek.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.google.gson.Gson
import com.moisha.snek.R
import com.moisha.snek.database.model.Level
import com.moisha.snek.glactivities.EditorActivity

class EditLevelActivity : LevelListActivity() {

    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listView: ListView = findViewById(R.id.add_level_list)

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val level: Level = parent?.getItemAtPosition(position) as Level

                val levelJson: String = gson.toJson(level)

                val editorIntent: Intent = Intent(
                    this@EditLevelActivity,
                    EditorActivity::class.java
                )
                editorIntent.putExtra("level", levelJson)

                startActivity(editorIntent)
            }
        }
    }

}