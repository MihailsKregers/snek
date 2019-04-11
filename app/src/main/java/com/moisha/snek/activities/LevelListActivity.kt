package com.moisha.snek.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.moisha.snek.R
import com.moisha.snek.database.DatabaseInstance
import com.moisha.snek.database.adapters.LevelAdapter
import com.moisha.snek.database.model.Level
import kotlinx.android.synthetic.main.activity_add_level.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

abstract class LevelListActivity : AppCompatActivity() {

    private var adapter: LevelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_level)

        adapter = LevelAdapter(this@LevelListActivity, arrayListOf<Level>())
        add_level_list.adapter = adapter

        doAsync {
            val data: Collection<Level> = getVals()
            uiThread {
                adapter!!.addAll(data)
            }
        }

    }

    private fun getVals(): List<Level> {
        return DatabaseInstance.getInstance(this@LevelListActivity).levelDao().all
    }
}
