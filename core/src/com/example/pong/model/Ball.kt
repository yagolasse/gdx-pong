package com.example.pong.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.badlogic.gdx.utils.Disposable
import com.example.pong.pool.RepeatActionPool
import com.example.pong.util.createPool
import com.example.pong.util.getNewRandomVelocity
import com.example.pong.util.middleYPoint
import java.util.*

private const val ZERO = 0f

class Ball(
    private val initialX: Float,
    private val initialY: Float,
    private val texture: Texture,
    private val maxSpeed: Float = 5f,
    private val initialSpeed: Float = 3f,
    private val initialMaxHalfAngle: Float = 30f,
    private val paddleHitDeflectionAngle: Float = 65f
) : Actor(), Disposable {

    private val sprite = Sprite(texture)
    private val random = Random()

    private val actionPool = createPool {
        MoveByAction().apply {
            setAmount(xSpeed, ySpeed)
        }
    }.apply {
        fill(1)
    }

    private val repeatActionPool = RepeatActionPool(actionPool).apply {
        fill(1)
    }

    private var xSpeed = 0f
    private var ySpeed = 0f

    init {
        resetPosition()
        restartMoving(isStopped = true)
    }

    fun handleCollisionWithLeftPaddle(paddlesRectangle: Rectangle) {
        handleCollisionWithPaddle(false, paddlesRectangle)
    }

    fun handleCollisionWithRightPaddle(paddlesRectangle: Rectangle) {
        handleCollisionWithPaddle(true, paddlesRectangle)
    }

    fun handleCollisionWithVerticalBarriers(barrierRectangle: Rectangle) {
        if (sprite.boundingRectangle.overlaps(barrierRectangle)) {
            revertYSpeed()
        }
        handleSpeedChange()
    }

    fun handleCollisionWithHorizontalBarriers(barrierRectangle: Rectangle) {
        if (sprite.boundingRectangle.overlaps(barrierRectangle)) {
            resetPosition()
            restartMoving()
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

    private fun restartMoving(isStopped: Boolean = false) {
        random.getNewRandomVelocity(initialSpeed, initialMaxHalfAngle).also { (newXSpeed, newYSpeed) ->
            xSpeed = newXSpeed
            ySpeed = newYSpeed
        }
        if (!isStopped) {
            (actions[0] as? RepeatAction)?.let {
                removeAction(it)
                repeatActionPool.free(it)
            }
        }
        addAction(repeatActionPool.obtain())
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

    private fun handleSpeedChange() {
        (actions[0] as? RepeatAction)?.let {
            removeAction(it)
            repeatActionPool.free(it)
        }
        addAction(repeatActionPool.obtain())
    }

    private fun handleCollisionWithPaddle(shouldGoLeft: Boolean, paddlesRectangle: Rectangle) {
        if (sprite.boundingRectangle.overlaps(paddlesRectangle)) {
            val shouldGoDown =
                paddlesRectangle.middleYPoint > sprite.boundingRectangle.middleYPoint
            random.getNewRandomVelocity(
                maxSpeed,
                paddleHitDeflectionAngle,
                shouldGoLeft,
                shouldGoDown
            ).also { (newXSpeed, newYSpeed) ->
                xSpeed = newXSpeed
                ySpeed = newYSpeed
            }
        }
        handleSpeedChange()
    }

}
