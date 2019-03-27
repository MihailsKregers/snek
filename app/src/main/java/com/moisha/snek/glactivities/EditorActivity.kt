package com.moisha.snek.glactivities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.moisha.snek.R
import com.moisha.snek.graphics.surfaces.EditorSurface

class EditorActivity : AppCompatActivity() {

    private lateinit var mGLView: EditorSurface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        mGLView = EditorSurface(this, 5, 3)
        setContentView(mGLView)
    }
}
