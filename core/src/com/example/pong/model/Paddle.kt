package com.example.pong.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.badlogic.gdx.utils.Disposable
import com.example.pong.SCREEN_HEIGHT

class Paddle(
    private val initialX: Float,
    private val initialY: Float,
    private val texture: Texture,
    private val screenPadding: Float = 8f,
    private val movementAmount: Float = 10f
) : Actor(), Disposable {

    private val sprite = Sprite(texture)
    private val moveAction = MoveByAction()

    private val repeatAction = RepeatAction().apply {
        action = moveAction
        count = RepeatAction.FOREVER
    }

    private val topOffset = SCREEN_HEIGHT - sprite.height - screenPadding

    val boundingRectangle: Rectangle
        get() = sprite.boundingRectangle

    init {
        setPosition(initialX, initialY)
        with(sprite) {
            setPosition(initialX, initialY)
            setBounds(x, y, width, height)
        }
    }

    fun moveUp() {
        if (y <= topOffset) {
            moveAction.amountY = movementAmount
            checkMoveAction()
        }
    }

    fun moveDown() {
        if (y >= screenPadding) {
            moveAction.amountY = -movementAmount
            checkMoveAction()
        }
    }

    fun stopMoving() {
        removeAction(repeatAction)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        sprite.draw(batch)
    }

    override fun positionChanged() {
        checkShouldStop()
        sprite.setPosition(x, y)
    }

    override fun dispose() {
        texture.dispose()
    }

    private fun checkShouldStop() {
        when {
            y > topOffset -> {
                stopMoving()
                y = topOffset
            }
            y < screenPadding -> {
                stopMoving()
                y = screenPadding
            }
        }
    }

    private fun checkMoveAction() {
        if (!actions.contains(repeatAction)) {
            addAction(repeatAction)
        }
    }

}
