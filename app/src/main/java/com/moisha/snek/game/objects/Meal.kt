package com.moisha.snek.game.objects

/**
 * Class for providing a meal object in game.
 */

class Meal {

    private lateinit var coords: IntArray

    //function creates new meal, checking if position is free in the flat by function provided as argument
    fun newMeal(random: () -> IntArray, isFree: (IntArray) -> Boolean) {
        var point: IntArray

        do {
            point = random()
        } while (!isFree(point))

        coords = point

    }

    val isMeal = fun(point: IntArray): Boolean {
        return point.contentEquals(point)
    }

    fun getMeal(): IntArray {
        return coords
    }

}