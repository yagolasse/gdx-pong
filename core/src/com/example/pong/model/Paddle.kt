package com.example.pong.model

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.badlogic.gdx.utils.Disposable
import com.example.pong.SCREEN_HEIGHT

class Paddle(
    private val initialX: Float,
    private val initialY: Float,
    private val texture: Texture,
    private val inputKeySet: Pair<Int, Int>,
    private val screenPadding: Float = 8f,
    private val movementAmount: Float = 10f
) : Actor(), Disposable, InputProcessor {

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

    override fun keyDown(keycode: Int): Boolean {
        val (upKey, downKey) = inputKeySet
        when {
            keycode == upKey && y <= topOffset -> moveUp()
            keycode == downKey && y >= screenPadding -> moveDown()
            else -> return false
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        val (upKey, downKey) = inputKeySet
        if (keycode == upKey || keycode == downKey) {
            stopMoving()
        }
        return false
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        sprite.draw(batch)
    }

    override fun positionChanged() {
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
        sprite.setPosition(x, y)
    }

    override fun dispose() {
        texture.dispose()
    }

    private fun moveUp() {
        moveAction.amountY = movementAmount
        checkMoveAction()
    }

    private fun moveDown() {
        moveAction.amountY = -movementAmount
        checkMoveAction()
    }

    private fun checkMoveAction() {
        if(!actions.contains(repeatAction)) {
            addAction(repeatAction)
        }
    }

    private fun stopMoving() {
        removeAction(repeatAction)
    }

    override fun keyTyped(character: Char) = false

    override fun mouseMoved(screenX: Int, screenY: Int) = false

    override fun scrolled(amountX: Float, amountY: Float) = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
}
