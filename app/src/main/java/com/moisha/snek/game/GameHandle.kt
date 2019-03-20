package com.moisha.snek.game

import com.moisha.snek.game.objects.Maze
import com.moisha.snek.game.objects.Meal
import com.moisha.snek.game.objects.Flat
import com.moisha.snek.game.objects.Snek

/**
 * Main game handler class.
 */

abstract class GameHandle {

    abstract val flat: Flat
    abstract val maze: Maze
    abstract val meal: Meal
    abstract val snek: Snek

    init {

    }

}