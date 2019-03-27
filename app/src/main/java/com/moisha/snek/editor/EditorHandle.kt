package com.moisha.snek.editor

import com.moisha.snek.database.model.Level

class EditorHandle(x: Int, y: Int, sourceX: Int, sourceY: Int) {

    private var editor: EditorField = EditorField(x, y)

    private var partSizeX: Float
    private var partSizeY: Float
    private var menuSizeY: Float

    constructor(level: Level, sourceX: Int, sourceY: Int) : this(level.size[0], level.size[1], sourceX, sourceY) {
        editor = EditorField(level)
    }

    init {
        menuSizeY = (2.0f / sourceY.toFloat()) * sourceX.toFloat() / 3.0f
        var freeSizeYpt: Int = sourceY - sourceX / 3
        if (freeSizeYpt / y < sourceX / x) {
            partSizeY = (2.0f - menuSizeY) / freeSizeYpt.toFloat() / y.toFloat()
            partSizeX = 2.0f / freeSizeYpt.toFloat() / y.toFloat()
        } else {
            partSizeX = 2.0f / sourceX.toFloat() / x.toFloat()
            partSizeY = (2.0f - menuSizeY) / sourceX.toFloat() / x.toFloat()
        }
    }

    fun getMenuField(): List<FloatArray> {
        return listOf(
            floatArrayOf(-1.0f, -1.0f, -1.0f, -1.0f + menuSizeY, 1.0f, -1.0f + menuSizeY, 1.0f, -1.0f),
            floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)
        )
    }
}