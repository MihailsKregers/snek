package com.moisha.snek.game.objects

/**
 * Class for providing room data in actual game.
 */

class Room(size_x: IntRange, size_y: IntRange) {

    //room coordinate limits
    private val x_range: IntRange = size_x
    private val y_range: IntRange = size_y

    fun keep_in_room(coord: IntArray): IntArray {
        if (coord[0] > x_range.last) return intArrayOf(x_range.first, coord[1])
        if (coord[0] < x_range.first) return intArrayOf(x_range.last, coord[1])
        if (coord[1] > y_range.last) return intArrayOf(coord[0], y_range.first)
        if (coord[1] < y_range.first) return intArrayOf(coord[0], y_range.last)
        return coord // if nothing happened earlier - return as it is
    }

}