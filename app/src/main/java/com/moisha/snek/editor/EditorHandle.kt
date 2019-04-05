package com.moisha.snek.editor

import com.moisha.snek.database.model.Level

class EditorHandle(x: Int, y: Int, sourceX: Int, sourceY: Int) {

    //editor field object
    private lateinit var editor: EditorField

    //actually used action on level
    /**
     * 1 - set Snek
     * 2 - set barrier
     * 3 - clear snek
     * 4 - clear barrier
     * 5 - change level size
     * 6 - save level
     * 0 - no action
     */
    private var action: Int = 0

    //level size
    private var x: Int = x
    private var y: Int = y

    //drawable area size in pixels
    private val sourceX: Int = sourceX
    private val sourceY: Int = sourceY

    //single field unit size in OpenGL coords
    private var partSizeX: Float
    private var partSizeY: Float

    //menu block height in OpenGL coords and pixels
    private var menuSizeY: Float
    private var menuSizeYpt: Int

    //single field unit size in pixels
    private var partPt: Int

    //field offsets by x on screen in OpenGL coords and pixels
    private var xOffsetPt: Int
    private var xOffset: Float

    //if editor was called for level editing
    constructor(level: Level, sourceX: Int, sourceY: Int) : this(level.size[0], level.size[1], sourceX, sourceY) {
        editor = EditorField(level)
    }

    init {

        //initializing all element sizes
        menuSizeY = (2.0f / sourceY.toFloat()) * sourceX.toFloat() / 6.0f

        menuSizeYpt = (menuSizeY * sourceY.toFloat() / 2.0f).toInt()

        val freeSizeYpt: Int = sourceY - sourceX / 6
        if (freeSizeYpt / y < sourceX / x) {
            partSizeY = (2.0f - menuSizeY) / y.toFloat()
            partSizeX = partSizeY / sourceX.toFloat() * sourceY.toFloat()
        } else {
            partSizeX = 2.0f / x.toFloat()
            partSizeY = partSizeX / sourceY.toFloat() * sourceX.toFloat()
        }

        partPt = (partSizeY / 2 * sourceY).toInt()

        xOffset = (2.0f - partSizeX * x) / 2
        xOffsetPt = (sourceX - (partPt * x)) / 2

    }

    //reaction on provided user click coords
    fun reactOnClick(xTouch: Int, yTouch: Int): Int {
        if (yTouch > sourceY - menuSizeYpt) {
            if (xTouch < sourceX / 6) {
                action = 1
            } else if (xTouch < sourceX / 6 * 2) {
                action = 2
            } else if (xTouch < sourceX / 6 * 3) {
                action = 3
                editor.clearSnek()
            } else if (xTouch < sourceX / 6 * 4) {
                action = 4
                editor.clearBarriers()
            } else if (xTouch < sourceX / 6 * 5) {
                action = 5
            } else {
                action = 6
            }
        } else {
            if (xTouch >= xOffsetPt && xTouch <= xOffsetPt + partPt * x && yTouch <= partPt * y) {

                val i: Int = (xTouch - xOffsetPt) / partPt
                val j: Int = (yTouch) / partPt

                when (action) {
                    1 -> {
                        editor.setSnek(i, j)
                    }
                    2 -> {
                        editor.setBarrier(i, j)
                    }
                }
            }
        }

        return if (action < 5) 0 else action
    }

    fun getRedrawData(): Array<List<FloatArray>> {

        //lists for squares and their colors in OpenGL coords
        val coords: MutableList<FloatArray> = mutableListOf()
        val colors: MutableList<FloatArray> = mutableListOf()

        //actual field state for drawing
        val field: Array<IntArray> = editor.getField()

        //field reading loop for adding data to drawing arrays
        for (i in 0..field.lastIndex) {
            for (j in 0..field[i].lastIndex) {
                coords.add(
                    floatArrayOf(
                        xOffset + -1.0f + partSizeX * i, 1.0f - partSizeY * j - partSizeY,
                        xOffset + -1.0f + partSizeX * i, 1.0f - partSizeY * j,
                        xOffset + -1.0f + partSizeX * i + partSizeX, 1.0f - partSizeY * j,
                        xOffset + -1.0f + partSizeX * i + partSizeX, 1.0f - partSizeY * j - partSizeY
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

        return arrayOf(coords.toList(), colors.toList())
    }

    //get Level object from editor
    fun getLevel(uId: Int): Level {
        return editor.getLevel(uId)
    }

    fun getName(): String {
        return editor.levelName
    }

    fun setName(name: String) {
        editor.levelName = name
    }

    fun getX(): Int {
        return x
    }

    fun getY(): Int {
        return y
    }

    fun resizeLevel(x: Int, y: Int) {

        action = 0

        this.x = x
        this.y = y

        calcOffsets()

        editor.changeSize(x, y)

    }

    //hardcoded menu view
    fun getMenuDrawData(): Array<List<FloatArray>> {

        //when menu draw data requested, editor should be already initialized
        //initialize it with given xy if it is not
        if (!::editor.isInitialized) {
            editor = EditorField(x, y)
        }

        val tr_coords: MutableList<FloatArray> = mutableListOf()
        val tr_colors: MutableList<FloatArray> = mutableListOf()
        val sq_coords: MutableList<FloatArray> = mutableListOf()
        val sq_colors: MutableList<FloatArray> = mutableListOf()

        val buttonSizeX: Float = 2.0f / 6.0f

        //adding Snek edit button
        sq_coords.add(
            floatArrayOf(
                -1.0f, -1.0f,
                -1.0f, -1.0f + menuSizeY,
                -1.0f + buttonSizeX, -1.0f + menuSizeY,
                -1.0f + buttonSizeX, -1.0f
            )
        )
        sq_colors.add(
            floatArrayOf(
                0.0f, 0.0f, 1.0f, 1.0f
            )
        )

        //adding barrier edit button
        sq_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX, -1.0f,
                -1.0f + buttonSizeX, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 2, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 2, -1.0f
            )
        )
        sq_colors.add(
            floatArrayOf(
                0.0f, 0.0f, 0.0f, 1.0f
            )
        )

        //adding clear Snek button
        sq_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 2, -1.0f,
                -1.0f + buttonSizeX * 2, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 3, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 3, -1.0f
            )
        )
        sq_colors.add(
            floatArrayOf(
                0.0f, 0.0f, 1.0f, 1.0f
            )
        )
        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 2, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 3, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 2.5f, -1.0f
            )
        )
        tr_colors.add(
            floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f
            )
        )

        //adding clear barriers button
        sq_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 3, -1.0f,
                -1.0f + buttonSizeX * 3, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 4, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 4, -1.0f
            )
        )
        sq_colors.add(
            floatArrayOf(
                0.0f, 0.0f, 0.0f, 1.0f
            )
        )
        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 3, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 4, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 3.5f, -1.0f
            )
        )
        tr_colors.add(
            floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f
            )
        )

        //adding clear Snek button
        sq_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 4, -1.0f,
                -1.0f + buttonSizeX * 4, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 5, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 5, -1.0f
            )
        )
        sq_colors.add(
            floatArrayOf(
                1.0f, 0.0f, 1.0f, 1.0f
            )
        )
        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 4, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 5, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 4.5f, -1.0f + (menuSizeY / 2)
            )
        )
        tr_colors.add(
            floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f
            )
        )
        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 4, -1.0f,
                -1.0f + buttonSizeX * 5, -1.0f,
                -1.0f + buttonSizeX * 4.5f, -1.0f + (menuSizeY / 2)
            )
        )
        tr_colors.add(
            floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f
            )
        )

        //adding save button
        sq_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 5, -1.0f,
                -1.0f + buttonSizeX * 5, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 6, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 6, -1.0f
            )
        )
        sq_colors.add(
            floatArrayOf(
                0.0f, 0.0f, 0.0f, 1.0f
            )
        )
        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 5.2f, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 6, -1.0f + (menuSizeY / 2),
                -1.0f + buttonSizeX * 5.2f, -1.0f
            )
        )
        tr_colors.add(
            floatArrayOf(
                0.0f, 1.0f, 0.0f, 1.0f
            )
        )

        return arrayOf(
            sq_coords.toList(),
            sq_colors.toList(),
            tr_coords.toList(),
            tr_colors.toList()
        )

    }

    private fun calcOffsets() {
        menuSizeY = (2.0f / sourceY.toFloat()) * sourceX.toFloat() / 6.0f

        menuSizeYpt = (menuSizeY * sourceY.toFloat() / 2.0f).toInt()

        val freeSizeYpt: Int = sourceY - sourceX / 6
        if (freeSizeYpt / y < sourceX / x) {
            partSizeY = (2.0f - menuSizeY) / y.toFloat()
            partSizeX = partSizeY / sourceX.toFloat() * sourceY.toFloat()
        } else {
            partSizeX = 2.0f / x.toFloat()
            partSizeY = partSizeX / sourceY.toFloat() * sourceX.toFloat()
        }

        partPt = (partSizeY / 2 * sourceY).toInt()

        xOffset = (2.0f - partSizeX * x) / 2
        xOffsetPt = (sourceX - (partPt * x)) / 2
    }
}