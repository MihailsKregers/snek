package com.moisha.snek.game

import com.moisha.snek.database.model.Highscore
import com.moisha.snek.database.model.Level
import com.moisha.snek.game.objects.Flat
import com.moisha.snek.game.objects.Maze
import com.moisha.snek.game.objects.Meal
import com.moisha.snek.game.objects.Snek

/**
 * Main game handler class.
 */

class Game(level: Level, uId: Int, speed: Int, state: State? = null) {

    private var temp: Int = 1

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

    val emptyField: Array<IntArray> = Array(level.size[0], { IntArray(level.size[1]) })

    private val uId: Int = uId
    private val id: Int = level.id
    private val speed: Int = speed

    private val flat: Flat =
        state?.let { Flat(level.size[0], level.size[1], state.flatAcc) } ?: Flat(level.size[0], level.size[1])
    private val maze: Maze = Maze(level.barriers)
    private val meal: Meal = state?.let { Meal(state.meal) } ?: Meal()
    private val snek: Snek = Snek(
        state?.snek ?: level.snek,
        state?.direction ?: level.direction
    )

    private var score = state?.score ?: 0

    private var pendingDirection = 0

    init {
        for (barrier in level.barriers) {
            emptyField[barrier[0]][barrier[1]] = -1
        }
    }

    val checkWay: (IntArray) -> Boolean = fun(point: IntArray): Boolean {
        println(temp++)
        return canGetToPoint(point)
    }

    init {
        if (state == null) {
            flat.calcAccessible(checkWay, maze.checkBarrier)
            meal.newMeal(flat.meal(snek.onPoint))
        }
    }

    fun getField(): Array<IntArray> {
        //getting copy of static empty field with barriers
        val field: Array<IntArray> = Array(
            emptyField.size,
            { index: Int ->
                emptyField[index].copyOf()
            }
        )

        //adding Snek to it
        val snek: List<IntArray> = this.snek.getSnek().reversed()
        for (i in snek) {
            field[i[0]][i[1]] = snek.indexOf(i) + 2
        }

        //adding meal
        val meal: IntArray = this.meal.getMeal()
        field[meal[0]][meal[1]] = Game.MEAL

        return field
    }

    fun move(): Boolean {
        if (pendingDirection > 0) {
            snek.setDirection(pendingDirection)
        }

        //moving Snek and getting result of this action
        val result: Int = snek.moveByDirection(meal.isMeal, maze.checkBarrier, flat.keepInFlat)

        when (result) {
            1 -> {
                //if meal was eaten by Snek, increase score and set new meal, if it's possible
                score++
                return meal.newMeal(flat.meal(snek.onPoint))
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

        return false //if nothing returned earlier - game logic error, also stop game
    }

    fun getResult(): Highscore {
        return Highscore(uId, id, score, speed)
    }

    fun getState(): State {
        return State(snek.getSnek(), flat.getAcc(), snek.getDirection(), meal.getMeal(), score)
    }

    fun setDirection(direction: Int) {
        if (snek.allowedDirection(direction)) {
            this.pendingDirection = direction
        }
    }

    private fun MutableList<IntArray>.deepContains(value: IntArray): Boolean {
        this.forEach {
            if (it.contentEquals(value)) return true
        }
        return false
    }

    private fun canGetToPoint(point: IntArray, way: MutableList<IntArray> = mutableListOf()): Boolean {
        if (snek.onPoint(point)) {
            return true
        } else {
            way.add(point)
        }

        val nextPoints: List<IntArray> = listOf(
            flat.keepInFlat(intArrayOf(point[0] + 1, point[1])),
            flat.keepInFlat(intArrayOf(point[0] - 1, point[1])),
            flat.keepInFlat(intArrayOf(point[0], point[1] + 1)),
            flat.keepInFlat(intArrayOf(point[0], point[1] - 1))
        )
        nextPoints.forEach {
            if (maze.checkBarrier(it) && !way.deepContains(it)) {
                if (canGetToPoint(it, way)) {
                    return true
                }
            }
        }
        return false
    }

}