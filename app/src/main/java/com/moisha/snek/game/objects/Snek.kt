package com.moisha.snek.game.objects

/**
 * Class for handling a snek as game object
 */

import kotlin.math.abs

class Snek(start_snek: Collection<IntArray>) {

    // list of xy coordinates of snek location
    private val state: MutableList<IntArray> = start_snek.toMutableList()

    // actual direction of snek moving
    // 1 - up, 2 - right, 3 - down, 4 - left
    private var direction: Int = 1 //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! - NOT IMPLEMENTED!

    // sets new direction, if it is not opposite to actual and not same as actual
    fun set_direction(new_direction: Int) {
        if (abs(this.direction - new_direction) != 2) {
            this.direction = new_direction
        }
    }


    // moves the snek to the next position, according to actual direction
    // returns true if move was successfull or false if game lost
    // checks meals, barriers and room boundaries by functions provided as arguments
    fun move_by_direction(
        meal: (IntArray) -> Boolean,
        bar_check: (IntArray) -> Boolean,
        in_room: (IntArray) -> IntArray
    ): Boolean {

        val new_head = in_room(new_head()) // getting new sneks head position, keeping snek in room

        if (state.indexOf(new_head) > 0) return false // if snek trying to go over itself - return false

        // move whole snek or grow to the direction of moving, if meal was eaten
        state.add(new_head)
        if (!meal(new_head) && !bar_check(new_head)) state.removeAt(0)

        return true // all done successfully - return true
    }

    private fun new_head(): IntArray {
        val head: IntArray = state.last()

        when (this.direction) { // calculating new sneks head position
            1 -> head[0]++
            3 -> head[0]--
            2 -> head[1]++
            4 -> head[1]--
        }

        return head
    }
}