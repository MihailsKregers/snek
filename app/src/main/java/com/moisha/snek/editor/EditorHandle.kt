package com.moisha.snek.editor

import com.moisha.snek.database.model.Level

class EditorHandle(x: Int, y: Int, sourceX: Int, sourceY: Int) {

    private var editor: EditorField = EditorField(x, y)

    private var partSizeX: Float
    private var partSizeY: Float
    private var menuSizeY: Float
    private var partPt: Int
    private var xMove: Float

    //last state, that was requestet for drawing
    private var screenState: Array<IntArray> = Array<IntArray>(x, { IntArray(y, { -2 }) })

    constructor(level: Level, sourceX: Int, sourceY: Int) : this(level.size[0], level.size[1], sourceX, sourceY) {
        editor = EditorField(level)
    }

    constructor(level: Level, x: Int, y: Int, sourceX: Int, sourceY: Int) : this(x, y, sourceX, sourceY) {
        editor = EditorField(level)
        editor.changeSize(x, y)
    }

    init {
        menuSizeY = (2.0f / sourceY.toFloat()) * sourceX.toFloat() / 6.0f
        var freeSizeYpt: Int = sourceY - sourceX / 6
        if (freeSizeYpt / y < sourceX / x) {
            partSizeY = (2.0f - menuSizeY) / y.toFloat()
            partSizeX = partSizeY / sourceX.toFloat() * sourceY.toFloat()
        } else {
            partSizeX = 2.0f / x.toFloat()
            partSizeY = partSizeX / sourceY.toFloat() * sourceX.toFloat()
        }
        partPt = (partSizeY / 2 * sourceY).toInt()
        xMove = (2.0f - partSizeX * x) / 2

        println(partPt)

        editor.setBarrier(0, 3)
        editor.setBarrier(0, 2)
        editor.setBarrier(5, 0)
        editor.setBarrier(5, 1)
        editor.setSnek(0, 0)
        editor.setSnek(0, 1)
        editor.setSnek(1, 1)
        editor.setSnek(1, 2)
        editor.setSnek(2, 2)
        editor.setSnek(3, 2)
        editor.setSnek(3, 3)
        editor.setSnek(3, 0)
    }

    fun getMenuField(): List<FloatArray> {
        return listOf(
            floatArrayOf(-1.0f, -1.0f, -1.0f, -1.0f + menuSizeY, 1.0f, -1.0f + menuSizeY, 1.0f, -1.0f),
            floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)
        )
    }

    fun getRedrawData(): Array<List<FloatArray>> {
        val coords: MutableList<FloatArray> = mutableListOf()
        val colors: MutableList<FloatArray> = mutableListOf()
        val field: Array<IntArray> = editor.getField()
        for (i in 0..field.lastIndex) {
            for (j in 0..field[i].lastIndex) {
                if (field[i][j] == screenState[i][j]) continue
                coords.add(
                    floatArrayOf(
                        xMove + -1.0f + partSizeX * i, 1.0f - partSizeY * j - partSizeY,
                        xMove + -1.0f + partSizeX * i, 1.0f - partSizeY * j,
                        xMove + -1.0f + partSizeX * i + partSizeX, 1.0f - partSizeY * j,
                        xMove + -1.0f + partSizeX * i + partSizeX, 1.0f - partSizeY * j - partSizeY
                    )
                )
                when (field[i][j]) {
                    0 -> colors.add(
                        floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
                    )
                    1 -> colors.add(
                        floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)
                    )
                    -1 -> colors.add(
                        floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
                    )
                    else -> {
                        colors.add(
                            floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)
                        )
                    }
                }
            }
        }
        screenState = field.clone()

        return arrayOf(coords.toList(), colors.toList())
    }
}