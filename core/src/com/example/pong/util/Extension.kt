package com.example.pong.util

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool
import java.util.*
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

val Rectangle.middleYPoint
    get() = y + (height / 2)

fun toRadians(degrees: Float): Float = degrees * Math.PI.toFloat() / 180f

fun <T> createPool(onNewObject: () -> T) = object : Pool<T>() {
    override fun newObject() = onNewObject()
}

fun Random.getNewRandomVelocity(
    baseSpeed: Float,
    halfMaxAngle: Float,
    shouldGoLeft: Boolean = nextBoolean(),
    shouldGoDown: Boolean = nextBoolean()
): Pair<Float, Float> {
    val randomAngle = nextFloat() * halfMaxAngle
    val radians = toRadians(randomAngle)
    var xSpeed = cos(radians) * baseSpeed
    var ySpeed = sin(radians) * baseSpeed
    if(shouldGoLeft) xSpeed = -xSpeed
    if(shouldGoDown) ySpeed = -ySpeed
    return xSpeed to ySpeed
}
