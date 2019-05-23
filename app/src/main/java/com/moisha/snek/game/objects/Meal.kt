package com.moisha.snek.game.objects

/**
 * Class for providing a meal object in game.
 */

class Meal(coords: IntArray = intArrayOf(-1, -1)) {

    private var coords: IntArray = coords

    //function creates new meal, checking if position is free in the flat by function provided as argument
    fun newMeal(point: IntArray): Boolean {
        this.coords = point
        return !point.contentEquals(
            intArrayOf(-1, -1)
        )
    }

    val isMeal = fun(point: IntArray): Boolean {
        return point.contentEquals(this.coords)
    }

    fun getMeal(): IntArray {
        return this.coords
    }

}