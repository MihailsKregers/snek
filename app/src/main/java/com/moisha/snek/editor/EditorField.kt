package com.moisha.snek.editor

import com.moisha.snek.database.model.Level
import java.lang.Integer.min

/**
 * Class for handling editor game field
 * Represented as 2-dimensional integer array
 * Values meaning:
 *      0 - empty field
 *      -1 - barrier
 *      1 - direction
 *      >1 - Snek
 */

class EditorField(x: Int, y: Int) {

    private var x: Int = x
    private var y: Int = y
    private var field: Array<IntArray> = Array(x, { IntArray(y) })

    private var snekSize: Int = 0

    /**
     * PUBLIC
     */
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
        val headDirection = level.snek.last()
        when (level.direction) {
            1 -> headDirection[1] = if (headDirection[1] == y) 0 else headDirection[1] + 1
            2 -> headDirection[0] = if (headDirection[0] == x) 0 else headDirection[0] + 1
            3 -> headDirection[1] = if (headDirection[1] == 0) y else headDirection[1] - 1
            4 -> headDirection[0] = if (headDirection[0] == 0) x else headDirection[0] - 1
        }
        snekSize = 1
        for (i in level.snek) {
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
        val snek = readSnek(field).subList(0, snekSize - 2)
        val direction = readDirection()

        return Level(size, barriers, snek, direction, "Vova")
    }

    fun getData(): List<List<IntArray>> {
        //list of data to be returned
        val data: MutableList<List<IntArray>> = mutableListOf()

        //adding level size
        data.add(listOf(intArrayOf(this.x, this.y)))
        //adding barrier list
        data.add(barList())
        //adding Snek
        data.add(readSnek(field))

        return data.toList()
    }

    fun getSnekSize(): Int {
        return snekSize
    }

    /**
     * PRIVATE (internal)
     */

    private fun barList(): List<IntArray> {
        //barrier list to be returned
        val barriers: MutableList<IntArray> = mutableListOf()

        //finding barriers in field
        for (i in 0..this.x - 1) {
            for (j in 0..this.y - 1) {
                if (field[i][j] == -1) barriers.add(intArrayOf(i, j))
            }
        }

        return barriers.toList()
    }

    private fun copyBar(newField: Array<IntArray>): Array<IntArray> {
        for (i: Int in 0..min(field.size - 1, newField.size - 1)) {
            for (j: Int in 0..min(field[i].size - 1, newField[i].size - 1)) {
                if (field[i][j] == -1) {
                    newField[i][j] = -1
                }
            }
        }

        return newField
    }

    private fun copySnek(newField: Array<IntArray>): Array<IntArray> {
        //list for snek coords to be copied
        val snek: List<IntArray> = readSnek(newField)

        //setting new Snek size
        snekSize = snek.size

        //adding Snek to new field
        for (snekpart in snek) {
            newField[snekpart[0]][snekpart[1]] = snek.indexOf(snekpart) + 1
        }

        //return new field with added Snek
        return newField
    }

    private fun readSnek(newField: Array<IntArray>): List<IntArray> {
        //list for snek coords to be returned
        val snek: MutableList<IntArray> = mutableListOf()
        var newSnekSize = 0

        //finding Sneks beginning, if it located within the limits of field
        all@ for (i: Int in 0..min(newField.size - 1, field.size - 1)) {
            for (j: Int in 0..min(newField[i].size - 1, field[i].size - 1)) {
                if (field[i][j] == 1) {
                    snek.add(intArrayOf(i, j))
                    newSnekSize++
                    break@all
                }
            }
        }

        //loop for finding continuous part of snek from beginning within the limits of provided field
        while (snek.isNotEmpty()) {
            if (snek.last()[0] + 1 < newField.size &&
                field[snek.last()[0] + 1][snek.last()[1]] == snek.size + 1
            ) {

                snek.add(intArrayOf(snek.last()[0] + 1, snek.last()[1]))

            } else if (snek.last()[0] - 1 >= 0 &&
                field[snek.last()[0] + 1][snek.last()[1]] == snek.size + 1
            ) {

                snek.add(intArrayOf(snek.last()[0] - 1, snek.last()[1]))

            } else if (snek.last()[1] + 1 < newField[0].size &&
                field[snek.last()[0]][snek.last()[1] + 1] == snek.size + 1
            ) {

                snek.add(intArrayOf(snek.last()[0], snek.last()[1] + 1))

            } else if (snek.last()[1] - 1 >= 0 &&
                field[snek.last()[0]][snek.last()[1] - 1] == snek.size + 1
            ) {

                snek.add(intArrayOf(snek.last()[0], snek.last()[1] - 1))

            } else {
                snekSize = newSnekSize
                return snek.toList()
            }
            newSnekSize++
        }

        snekSize = newSnekSize
        return snek.toList()
    }

    private fun readDirection(): Int {
        val dir = readSnek(field).first()
        val head = readSnek(field).get(1)
        if (dir[1] > head[1]) {
            return 1
        } else if (dir[0] > head[0]) {
            return 2
        } else if (dir[1] < head[1]) {
            return 3
        } else if (dir[0] < head[0]) {
            return 4
        }
        return -1
    }

}