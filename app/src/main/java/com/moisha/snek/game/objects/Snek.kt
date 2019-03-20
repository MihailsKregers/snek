package com.moisha.snek.game.objects

/**
 * Class for handling a snek as game object
 */

import kotlin.math.abs

class Snek(startSnek: Collection<IntArray>) {

    // list of xy coordinates of snek location
    private val state: MutableList<IntArray> = startSnek.toMutableList()

    // actual direction of snek moving
    // 1 - up, 2 - right, 3 - down, 4 - left
    private var direction: Int = 1 //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! - NOT IMPLEMENTED!

    // sets new direction, if it is not opposite to actual and not same as actual
    fun setDirection(newDirection: Int) {
        if (abs(this.direction - newDirection) != 2) {
            this.direction = newDirection
        }
    }


    // moves the snek to the next position, according to actual direction
    // returns true if move was successfull or false if game lost
    // checks meals, barriers and flat boundaries by functions provided as arguments
    fun moveByDirection(
        meal: (IntArray) -> Boolean,
        barCheck: (IntArray) -> Boolean,
        inRoom: (IntArray) -> IntArray
    ): Boolean {

        val newHead = inRoom(newHead()) // getting new sneks head position, keeping snek in flat

        if (state.indexOf(newHead) > 0) return false // if snek trying to go over itself - return false

        // move whole snek or grow to the direction of moving, if meal was eaten
        state.add(newHead)
        if (!meal(newHead) && !barCheck(newHead)) state.removeAt(0)

        return true // all done successfully - return true
    }

    private fun newHead(): IntArray {
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