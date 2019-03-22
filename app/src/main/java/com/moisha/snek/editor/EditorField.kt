package com.moisha.snek.editor

import com.moisha.snek.database.model.Level
import com.moisha.snek.editor.internal.EditorFieldInternal

/**
 * Class for handling editor game field
 * Represented as 2-dimensional integer array
 * Values meaning:
 *      0 - empty field
 *      -1 - barrier
 *      1 - direction
 *      >1 - Snek
 */

class EditorField(x: Int, y: Int) : EditorFieldInternal(x, y) {

    constructor(level: Level) : this(level.size[0], level.size[1]) {
        //unpacking size
        x = level.size[0]
        y = level.size[1]

        //initializing field array
        this.field = Array(x, { IntArray(y) })

        //unpacking barriers
        for (i in level.barriers) {
            setBarrier(i[0], i[1])
        }

        //unpacking snek
        val head: IntArray = level.snek.last()
        when (level.direction) {
            1 -> if (head[1] == y - 1) field[head[0]][0] = 1 else field[head[0]][head[1] + 1] = 1
            2 -> if (head[0] == x - 1) field[0][head[1]] = 1 else field[head[0] + 1][head[1]] = 1
            3 -> if (head[1] == 0) field[head[0]][y - 1] = 1 else field[head[0]][head[1] - 1] = 1
            4 -> if (head[0] == 0) field[x - 1][head[1]] = 1 else field[head[0] - 1][head[1]] = 1
        }
        snekSize = 1

        for (i in level.snek.reversed()) {
            snekSize++
            field[i[0]][i[1]] = snekSize
        }
    }

    fun getField(): Array<IntArray> {
        return field
    }

    fun setSnek(x: Int, y: Int): Boolean {
        //if it's the end of Sneks tail - remove it and return true
        if (field[x][y] == snekSize && snekSize != 0) {
            snekSize--
            field[x][y] = 0
            return true
        }

        //if it's behind the end of Sneks tail and coords is free - grow to coords and return true
        if (field[x][y] == 0 && (
                    field[if (x + 1 >= this.x) 0 else x + 1][y] == snekSize ||
                            field[if (x - 1 < 0) this.x - 1 else x - 1][y] == snekSize ||
                            field[x][if (y + 1 >= this.y) 0 else y + 1] == snekSize ||
                            field[x][if (y - 1 < 0) this.y - 1 else y - 1] == snekSize
                    )
        ) {
            snekSize++
            field[x][y] = snekSize
            return true
        }

        //if nothing happened - return false
        return false
    }

    fun setBarrier(x: Int, y: Int): Boolean {
        //change state, if empty or barrier
        when (field[x][y]) {
            -1 -> {
                field[x][y] = 0
            }
            0 -> {
                field[x][y] = -1
            }
            else -> {
                //if nothing happened with field
                return false
            }
        }
        //if not returned false - something happened, return true
        return true
    }

    fun clearSnek(): Boolean {
        //if no Snek in field - return false
        if (snekSize == 0) return false

        //else clear Snek and return true
        for (i in 0..x - 1) {
            for (j in 0..y - 1) {
                if (field[i][j] > 0) {
                    field[i][j] = 0
                    snekSize--
                }
            }
        }

        return true
    }

    fun clearBarriers() {
        for (i in 0..x - 1) {
            for (j in 0..y - 1)
                if (field[i][j] == -1) field[i][j] = 0
        }
    }

    fun changeSize(x: Int, y: Int) {
        //making new field of provided size
        var newField: Array<IntArray> = Array(x, { IntArray(y) })

        //copying barriers and Snek to new field
        newField = copySnek(copyBar(newField))

        //setting new size values
        this.x = x
        this.y = y

        //setting new field
        field = newField
    }

    fun getLevel(name: String): Level {
        val size = intArrayOf(this.x, this.y)
        val barriers = barList()
        val snek = readSnek()
        val direction = readDirection(snek)

        //subList works from inclusive to EXCLUSIVE!!! subList length is snek length -1
        return Level(size, barriers, snek.subList(0, snek.lastIndex), direction, name)
    }

    fun getSnekSize(): Int {
        return snekSize
    }
}