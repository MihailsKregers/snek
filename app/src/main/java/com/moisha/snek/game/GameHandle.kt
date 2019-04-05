package com.moisha.snek.game

import com.moisha.snek.database.model.Level
import com.moisha.snek.game.objects.*

/**
 * Main game handler class.
 */

class GameHandle(level: Level, uId: Int, sourceX: Int, sourceY: Int) : Game(level, uId) {

    //level size
    private val x: Int = level.size[0]
    private val y: Int = level.size[1]

    //single field unit size in OpenGL coords
    private val partSizeX: Float
    private val partSizeY: Float

    //menu block height in OpenGL coords and pixels
    private val menuSizeY: Float
    private val menuSizeYpt: Int

    //single field unit size in pixels
    private val partPt: Int

    //field offsets by x on screen in OpenGL coords and pixels
    private val xOffsetPt: Int
    private val xOffset: Float

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

    fun getRedrawData(): Array<List<FloatArray>> {

        //lists of squares and their colors in OpenGL coords
        val coords: MutableList<FloatArray> = mutableListOf()
        val colors: MutableList<FloatArray> = mutableListOf()

        //adding snek drawing data
        for (i in snek.getSnek()) {
            coords.add(
                floatArrayOf(
                    xOffset + -1.0f + partSizeX * i[0], 1.0f - partSizeY * i[1] - partSizeY,
                    xOffset + -1.0f + partSizeX * i[0], 1.0f - partSizeY * i[1],
                    xOffset + -1.0f + partSizeX * i[0] + partSizeX, 1.0f - partSizeY * i[1],
                    xOffset + -1.0f + partSizeX * i[0] + partSizeX, 1.0f - partSizeY * i[1] - partSizeY
                )
            )
            colors.add(
                floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)
            )
        }

        //adding meal drawing data
        val point: IntArray = meal.getMeal()

        coords.add(
            floatArrayOf(
                xOffset + -1.0f + partSizeX * point[0], 1.0f - partSizeY * point[1] - partSizeY,
                xOffset + -1.0f + partSizeX * point[0], 1.0f - partSizeY * point[1],
                xOffset + -1.0f + partSizeX * point[0] + partSizeX, 1.0f - partSizeY * point[1],
                xOffset + -1.0f + partSizeX * point[0] + partSizeX, 1.0f - partSizeY * point[1] - partSizeY
            )
        )
        colors.add(
            floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f)
        )

        return arrayOf(
            coords,
            colors
        )

    }

    fun getMenuDrawData(): Array<List<FloatArray>> {

        val coords: MutableList<FloatArray> = mutableListOf()
        val colors: MutableList<FloatArray> = mutableListOf()
        val tr_coords: MutableList<FloatArray> = mutableListOf()
        val tr_colors: MutableList<FloatArray> = mutableListOf()

        // adding flat backgroung and barriers to menu drawing data,
        // to be processed as static objects, not recalculated for every frame
        coords.add(
            floatArrayOf(
                -1.0f + xOffset, 1.0f - (partSizeY * y),
                -1.0f + xOffset, 1.0f,
                1.0f + xOffset, 1.0f,
                1.0f - xOffset, 1.0f - (partSizeY * y)
            )
        )
        colors.add(
            floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
        )

        for (i in maze.getBarriers()) {
            coords.add(
                floatArrayOf(
                    xOffset + -1.0f + partSizeX * i[0], 1.0f - partSizeY * i[1] - partSizeY,
                    xOffset + -1.0f + partSizeX * i[0], 1.0f - partSizeY * i[1],
                    xOffset + -1.0f + partSizeX * i[0] + partSizeX, 1.0f - partSizeY * i[1],
                    xOffset + -1.0f + partSizeX * i[0] + partSizeX, 1.0f - partSizeY * i[1] - partSizeY
                )
            )
            colors.add(
                floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
            )
        }

        // hardcoded menu draw data

        val buttonSizeX: Float = 2.0f / 6.0f

        coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX, -1.0f,
                -1.0f + buttonSizeX, -1.0f + menuSizeY,
                1.0f - buttonSizeX, -1.0f + menuSizeY,
                1.0f - buttonSizeX, -1.0f
            )
        )
        colors.add(
            floatArrayOf(0.0f, 1.0f, 1.0f, 1.0f)
        )

        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 1.9f, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 1.1f, -1.0f + (menuSizeY / 2),
                -1.0f + buttonSizeX * 1.9f, -1.0f
            )
        )
        tr_colors.add(
            floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f
            )
        )
        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 2, -1.0f + (menuSizeY * 0.9f),
                -1.0f + buttonSizeX * 2.5f, -1.0f + (menuSizeY * 0.1f),
                -1.0f + buttonSizeX * 3, -1.0f + (menuSizeY * 0.9f)
            )
        )
        tr_colors.add(
            floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f
            )
        )
        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 3, -1.0f + (menuSizeY * 0.1f),
                -1.0f + buttonSizeX * 3.5f, -1.0f + (menuSizeY * 0.9f),
                -1.0f + buttonSizeX * 4, -1.0f + (menuSizeY * 0.1f)
            )
        )
        tr_colors.add(
            floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f
            )
        )
        tr_coords.add(
            floatArrayOf(
                -1.0f + buttonSizeX * 4.1f, -1.0f + menuSizeY,
                -1.0f + buttonSizeX * 4.9f, -1.0f + (menuSizeY / 2),
                -1.0f + buttonSizeX * 4.1f, -1.0f
            )
        )
        tr_colors.add(
            floatArrayOf(
                1.0f, 1.0f, 0.0f, 1.0f
            )
        )
        return arrayOf(
            coords.toList(),
            colors.toList(),
            tr_coords.toList(),
            tr_colors.toList()
        )
    }

}