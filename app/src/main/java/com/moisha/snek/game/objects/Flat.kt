package com.moisha.snek.game.objects

import kotlin.random.Random

/**
 * Class for providing flat data in actual game.
 */

class Flat(size_x: Int, size_y: Int) {

    // flat coordinate limits
    private val x_range: IntRange = IntRange(0, size_x - 1)
    private val y_range: IntRange = IntRange(0, size_y - 1)

    fun keep_in_flat(coord: IntArray): IntArray {
        if (coord[0] > x_range.last) return intArrayOf(x_range.first, coord[1])
        if (coord[0] < x_range.first) return intArrayOf(x_range.last, coord[1])
        if (coord[1] > y_range.last) return intArrayOf(coord[0], y_range.first)
        if (coord[1] < y_range.first) return intArrayOf(coord[0], y_range.last)
        return coord // if nothing happened earlier - return as it is
    }

    // returns random point in flat boundaries
    fun random_point(): IntArray {
        return intArrayOf(Random.nextInt(x_range.first, x_range.last), Random.nextInt(y_range.first, y_range.last))
    }

}