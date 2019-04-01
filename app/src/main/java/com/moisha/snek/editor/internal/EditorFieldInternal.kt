package com.moisha.snek.editor.internal

import com.moisha.snek.R
import com.moisha.snek.global.App

abstract class EditorFieldInternal(x: Int, y: Int) {

    internal var x: Int = x
    internal var y: Int = y
    internal var field: Array<IntArray> = Array(x, { IntArray(y) })

    internal var snekSize: Int = 0

    var levelName: String = App.applicationContext().resources.getString(R.string.empty_level_name)

    protected fun barList(): List<IntArray> {
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

    protected fun copyBar(newField: Array<IntArray>): Array<IntArray> {
        for (i: Int in 0..(if (field.size > newField.size) newField.size - 1 else field.size - 1)) {
            for (j: Int in 0..(if (field[i].size > newField[i].size) newField[i].size - 1 else field[i].size - 1)) {
                if (field[i][j] == -1) {
                    newField[i][j] = -1
                }
            }
        }

        return newField
    }

    protected fun copySnek(newField: Array<IntArray>): Array<IntArray> {
        //list for snek coords to be copied
        val snek: List<IntArray> = readUnbrokenSnek(newField)

        //setting new Snek size
        snekSize = snek.size

        //adding Snek to new field
        for (snekpart in snek) {
            newField[snekpart[0]][snekpart[1]] = snek.indexOf(snekpart) + 1
        }

        //return new field with added Snek
        return newField
    }

    protected fun readUnbrokenSnek(newField: Array<IntArray>): List<IntArray> {
        //list for snek coords to be returned
        val snek: MutableList<IntArray> = mutableListOf()
        var newSnekSize = 0

        //finding Sneks beginning, if it located within the limits of field
        all@ for (i: Int in 0..(if (field.size > newField.size) newField.size - 1 else field.size - 1)) {
            for (j: Int in 0..(if (field[i].size > newField[i].size) newField[i].size - 1 else field[i].size - 1)) {
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

    protected fun readSnek(): List<IntArray> {

        val snek: MutableList<IntArray> = mutableListOf()

        element@ for (toFind: Int in 1..(if (snekSize > 0) snekSize else 1)) {
            for (i: Int in 0..field.size - 1) {
                for (j: Int in 0..field[i].size - 1) {
                    if (field[i][j] == toFind) {
                        snek.add(intArrayOf(i, j))
                        continue@element
                    }
                }
            }
        }

        return snek.toList().reversed()
    }

    protected fun readDirection(snek: List<IntArray>): Int {
        val dir = snek.last()
        val head = snek.get(snek.size - 2)
        if (dir[1] > head[1]) {
            return if (head[1] == 0) 3 else 1
        } else if (dir[0] > head[0]) {
            return if (head[0] == 0) 4 else 2
        } else if (dir[1] < head[1]) {
            return if (head[1] == y - 1 && dir[1] == 0) 1 else 3
        } else if (dir[0] < head[0]) {
            return if (head[0] == x - 1 && dir[1] == 0) 2 else 4
        }
        return -1
    }
}