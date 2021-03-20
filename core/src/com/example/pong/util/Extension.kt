package com.example.pong.util

import com.badlogic.gdx.math.Rectangle

val Rectangle.middleYPoint
    get() = y + (height / 2)

fun toRadians(degrees: Float): Float = degrees * Math.PI.toFloat() / 180f
