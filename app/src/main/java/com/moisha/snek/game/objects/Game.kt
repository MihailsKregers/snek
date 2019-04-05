package com.moisha.snek.game.objects

import com.moisha.snek.database.model.Highscore
import com.moisha.snek.database.model.Level

/**
 * Main game handler class.
 */

abstract class Game(level: Level, uId: Int) {

    private val uId: Int = uId
    private val id: Int = level.id

    private val flat: Flat = Flat(level.size[0], level.size[1])
    protected val maze: Maze = Maze(level.barriers)
    protected val meal: Meal = Meal()
    protected val snek: Snek = Snek(level.snek, level.direction)

    private var score = 0

    private var pendingDirection = 0

    val checkFree: (IntArray) -> Boolean = fun(point: IntArray): Boolean {
        return (maze.checkBarrier(point) && !snek.onPoint(point))
    }

    init {
        meal.newMeal(flat.randomPoint, checkFree)
    }

    fun move(): Boolean {
        if (pendingDirection > 0) {
            snek.setDirection(pendingDirection)
        }

        //moving Snek and getting result of this action
        val result: Int = snek.moveByDirection(meal.isMeal, maze.checkBarrier, flat.keepInFlat)

        when (result) {
            1 -> {
                //if meal was eaten by Snek, set new meal and increase score
                meal.newMeal(flat.randomPoint, checkFree)
                score++
                return true
            }
            0 -> {
                //meal wasn't eaten, but Snek successfully moved
                return true
            }
            -1 -> {
                //game over
                return false
            }
        }

        return false //if something not returned earlier - game logic error, also stop game
    }

    fun getScore(): Highscore {
        val result: Highscore = Highscore(uId, id, score)

        return result
    }

}