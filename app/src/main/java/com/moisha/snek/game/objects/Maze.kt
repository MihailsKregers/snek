package com.moisha.snek.game.objects

/**
 * Class for providing maze data for actual game.
 */

class Maze(barriers: Collection<IntArray>) {

    private val barriers: List<IntArray> = barriers.toList()

    // check if exists barrier on provided place
    val checkBarrier = fun(coord: IntArray): Boolean {

        barriers.forEach {
            //if on barrier
            if (it.contentEquals(coord)) return false
        }
        return true //if not on barrier

    }

    fun getBarriers(): List<IntArray> {
        return barriers
    }


}