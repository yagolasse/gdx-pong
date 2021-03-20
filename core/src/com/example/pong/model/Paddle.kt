package com.example.pong.model

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Disposable

private const val ZERO = 0f

class Paddle(
    val tag: String,
    private val initialX: Float,
    private val initialY: Float,
    private val texture: Texture,
    private val inputKeySet: Pair<Int, Int>,
    private val movementAmount: Float = 10f
) : Actor(), Disposable, InputProcessor {

    private val sprite = Sprite(texture)

    private var moveAction: Action? = null

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
        when (keycode) {
            upKey -> moveUp()
            downKey -> moveDown()
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
        super.positionChanged()
        sprite.setPosition(x, y)
    }

    override fun dispose() {
        texture.dispose()
    }

    private fun moveUp() {
        moveInTheYAxis(movementAmount)
    }

    private fun moveDown() {
        moveInTheYAxis(-movementAmount)
    }

    private fun moveInTheYAxis(yAmount: Float) {
        removeAction(moveAction)
        moveAction = Actions.forever(Actions.moveBy(ZERO, yAmount))
        addAction(moveAction)
    }

    private fun stopMoving() {
        removeAction(moveAction)
        moveAction = null
    }

    override fun keyTyped(character: Char) = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false

    override fun mouseMoved(screenX: Int, screenY: Int) = false

    override fun scrolled(amountX: Float, amountY: Float) = false
}
