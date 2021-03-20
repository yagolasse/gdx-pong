package com.example.pong.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Disposable
import com.example.pong.util.*
import java.util.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private const val ZERO = 0f

class Ball(
    private val initialX: Float,
    private val initialY: Float,
    private val texture: Texture,
    private val maxSpeed: Float = 5f,
    private val initialSpeed: Float = 3f,
    private val initialMaxHalfAngle: Float = 30f,
    private val paddleCollisionMaxHalfAngle: Float = 65f
) : Actor(), Disposable {

    private val sprite = Sprite(texture)
    private val random = Random()

    private var moveAction: Action? = null

    private var xSpeed = 0f
    private var ySpeed = 0f

    init {
        resetPosition()
        startMoving()
    }

    fun startMoving() {
        val isGoingDown = random.nextBoolean()
        val isGoingLeft = random.nextBoolean()

        getNewRandomVelocity(initialMaxHalfAngle, initialSpeed).also {
            xSpeed = it.first
            ySpeed = it.second
        }

        if (isGoingLeft) {
            xSpeed = -xSpeed
        }
        if (isGoingDown) {
            ySpeed = -ySpeed
        }

        removeAction(moveAction)
        moveAction = Actions.forever(Actions.moveBy(xSpeed, ySpeed)).also {
            addAction(it)
        }
    }

    fun checkCollision(tag: String, other: Rectangle) {
        if (sprite.boundingRectangle.overlaps(other)) {
            when(tag) {
                LEFT_PADDLE -> handleCollisionWithPaddle(false, other)
                RIGHT_PADDLE -> handleCollisionWithPaddle(true, other)
                TOP_BARRIER -> revertYSpeed()
                BOTTOM_BARRIER -> revertYSpeed()
                LEFT_BARRIER, RIGHT_BARRIER -> {
                    resetPosition()
                    startMoving()
                }
                else -> return
            }

            removeAction(moveAction)
            moveAction = Actions.forever(Actions.moveBy(xSpeed, ySpeed)).also {
                addAction(it)
            }
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        sprite.draw(batch)
    }

    override fun positionChanged() {
        super.positionChanged()
        sprite.setPosition(x, y)
    }

    override fun dispose() {
        texture.dispose()
    }

    private fun resetPosition() {
        val xOffset = sprite.width / 2
        val yOffset = sprite.height / 2
        val xWithOffset = initialX + xOffset
        val yWithOffset = initialY + yOffset

        setPosition(xWithOffset, yWithOffset)
        with(sprite) {
            setPosition(xWithOffset, yWithOffset)
            setBounds(x, y, width, height)
        }
    }

    private fun revertYSpeed() {
        ySpeed = -ySpeed
    }

    private fun handleCollisionWithPaddle(isRight: Boolean, paddleRectangle: Rectangle) {
        getNewRandomVelocity(paddleCollisionMaxHalfAngle, maxSpeed).also {
            xSpeed = it.first
            ySpeed = it.second
        }

        if (isRight) {
            xSpeed = -xSpeed
        }

        if (paddleRectangle.middleYPoint > sprite.boundingRectangle.middleYPoint) {
            ySpeed = -ySpeed
        }
    }

    private fun getNewRandomVelocity(halfMaxAngle: Float, baseSpeed: Float): Pair<Float, Float> {
        val angle = toRadians(random.nextFloat() * halfMaxAngle)
        val xSpeed = abs(cos(angle) * baseSpeed)
        val ySpeed = abs(sin(angle) * baseSpeed)
        return xSpeed to ySpeed
    }
}