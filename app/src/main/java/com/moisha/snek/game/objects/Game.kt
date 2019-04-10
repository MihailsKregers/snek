package com.moisha.snek.game.objects

import com.moisha.snek.database.model.Highscore
import com.moisha.snek.database.model.Level

/**
 * Main game handler class.
 */

class Game(level: Level, uId: Int) {

    companion object {
        const val DIRECTION_RIGHT = 1
        const val DIRECTION_DOWN = 2
        const val DIRECTOPN_LEFT = 3
        const val DIRECTION_UP = 4
        const val BARRIER = -1
        const val EMPTY_UNIT = 0
        const val MEAL = -2
        const val DIRECTION = 1
        const val SNEK_FROM = 2
    }

    const val emptyField: Array<IntArray> = Array(level.size[0], { IntArray(level.size[1]) })

    private val uId: Int = uId
    private val id: Int = level.id

    private val flat: Flat = Flat(level.size[0], level.size[1])
    private val maze: Maze = Maze(level.barriers)
    private val meal: Meal = Meal()
    private val snek: Snek = Snek(level.snek, level.direction)

    private var score = 0

    private var pendingDirection = 0

    init {
        for (barrier in level.barriers) {
            emptyField[barrier[0]][barrier[1]] = -1
        }
    }

    val checkFree: (IntArray) -> Boolean = fun(point: IntArray): Boolean {
        return (maze.checkBarrier(point) && !snek.onPoint(point))
    }

    init {
        meal.newMeal(flat.randomPoint, checkFree)
    }

    fun getField(): Array<IntArray> {
        val field: Array<IntArray> = Array(
            emptyField.size,
            { index: Int ->
                emptyField[index].copyOf()
            }
        )

        val snek: List<IntArray> = this.snek.getSnek()
        for (i in snek) {
            field[i[0]][i[1]] =
        }
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

    fun setDirection(direction: Int) {
        if (snek.allowedDirection(direction)) {
            this.pendingDirection = direction
        }
    }

}