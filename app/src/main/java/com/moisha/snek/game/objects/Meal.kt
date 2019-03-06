package com.moisha.snek.game.objects

/**
 * Class for providing a meal object in game.
 */

class Meal {

    var coords: IntArray = IntArray(2)

    //function creates new meal, checking if position is free in the flat by function provided as argument
    fun new_meal(random: () -> IntArray, is_free: (IntArray) -> Boolean) {
        var point: IntArray

        do {
            point = random()
        } while (!is_free(point))

        coords = point

    }

    fun is_meal(point: IntArray): Boolean {
        return point.contentEquals(point)
    }

}