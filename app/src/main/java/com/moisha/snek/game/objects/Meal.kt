package com.moisha.snek.game.objects

/**
 * Class for providing a meal object in game.
 */

class Meal {

    var coords: IntArray = IntArray(2)

    //function creates new meal, checking if position is free in the flat by function provided as argument
    fun newMeal(random: () -> IntArray, isFree: (IntArray) -> Boolean) {
        var point: IntArray

        do {
            point = random()
        } while (!isFree(point))

        coords = point

    }

    fun isMeal(point: IntArray): Boolean {
        return point.contentEquals(point)
    }

}