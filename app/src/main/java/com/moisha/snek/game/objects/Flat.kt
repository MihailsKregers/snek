package com.moisha.snek.game.objects

/**
 * Class for providing flat data in actual game.
 */

class Flat(sizeX: Int, sizeY: Int, acc: List<IntArray> = listOf()) {

    // flat coordinate limits
    private val xRange: IntRange = IntRange(0, sizeX - 1)
    private val yRange: IntRange = IntRange(0, sizeY - 1)
    private var accessible: List<IntArray> = acc

    val keepInFlat = fun(coord: IntArray): IntArray {
        if (coord[0] > xRange.last) return intArrayOf(xRange.first, coord[1])
        if (coord[0] < xRange.first) return intArrayOf(xRange.last, coord[1])
        if (coord[1] > yRange.last) return intArrayOf(coord[0], yRange.first)
        if (coord[1] < yRange.first) return intArrayOf(coord[0], yRange.last)
        return coord // if nothing happened earlier - return as it is
    }

    /**
     * Returns random point, where meal can be placed
     */
    fun meal(isSnek: (IntArray) -> Boolean): IntArray {
        val points: MutableList<IntArray> = mutableListOf()
        accessible.forEach {
            if (!isSnek(it)) points.add(it)
        }
        if (points.isNotEmpty())
            return points.random()
        else
            return intArrayOf(-1, -1)
    }

    private fun MutableList<IntArray>.deepContains(value: IntArray): Boolean {
        this.forEach {
            if (it.contentEquals(value)) return true
        }
        return false
    }

    fun calcAccessible(isAccessible: (IntArray) -> Boolean, notBarrier: (IntArray) -> Boolean) {
        val points: MutableList<IntArray> = mutableListOf()
        for (i in xRange.first..xRange.last) {
            for (j in yRange.first..yRange.last) {
                print(i)
                print(j)
                println()
                val point: IntArray = intArrayOf(i, j)
                if (notBarrier(point)) {
                    val ac: Boolean = isAccessible(point)
                    println(ac)
                    if (ac) {
                        points.add(point)
                    }
                }
            }
        }
        accessible = points.toList()
    }

    fun getAcc(): List<IntArray> {
        return accessible
    }

}