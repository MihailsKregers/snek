package com.moisha.snek.game

import java.io.Serializable

class State(snek: List<IntArray>, flatAcc: List<IntArray>, direction: Int, meal: IntArray, score: Int) : Serializable {

    val snek: List<IntArray> = snek
    val flatAcc: List<IntArray> = flatAcc
    val direction: Int = direction
    val meal: IntArray = meal
    val score: Int = score

}