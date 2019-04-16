package com.moisha.snek.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.moisha.snek.App
import com.moisha.snek.R
import com.moisha.snek.activities.gl.LevelPreviewActivity
import com.moisha.snek.database.DatabaseInstance
import com.moisha.snek.database.adapters.LevelAdapter
import com.moisha.snek.database.model.Level
import com.moisha.snek.utility.GsonStatic
import kotlinx.android.synthetic.main.activity_edit_levels.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditLevelActivity : AppCompatActivity() {

    private var adapter: LevelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_levels)

        adapter = LevelAdapter(this@EditLevelActivity, arrayListOf<Level>())
        edit_level_list.adapter = adapter

        doAsync {
            val data: Collection<Level> = getVals()
            uiThread {
                adapter!!.addAll(data)
            }
        }

        val listView: ListView = findViewById(R.id.edit_level_list)

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val level: Level = parent?.getItemAtPosition(position) as Level

                val levelJson: String = GsonStatic.packLevel(level)

                val previewIntent: Intent = Intent(
                    this@EditLevelActivity,
                    LevelPreviewActivity::class.java
                )
                previewIntent.putExtra("level", levelJson)

                startActivity(previewIntent)
            }
        }
    }

    private fun getVals(): List<Level> {
        val uId: Int = App.getUser()
        return DatabaseInstance.getInstance(this@EditLevelActivity).levelDao().getPlayerLevels(uId)
    }

}