package com.moisha.snek.game.objects

/**
 * Class for providing maze data for actual game.
 */

class Maze(barriers: Collection<IntArray>) {

    private val barriers: List<IntArray> = barriers.toList()

    // check if exists barrier on provided place
    private fun checkBarrier(coord: IntArray): Boolean {
        return !barriers.contains(coord)
    }


}