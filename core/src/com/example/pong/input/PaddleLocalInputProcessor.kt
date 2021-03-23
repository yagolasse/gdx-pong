package com.example.pong.input

import com.badlogic.gdx.InputProcessor
import com.example.pong.model.Paddle

class PaddleLocalInputProcessor(
    private val upKey: Int,
    private val downKey: Int,
    private val paddle: Paddle
): InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            upKey -> paddle.moveUp()
            downKey -> paddle.moveDown()
            else -> return false
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == upKey || keycode == downKey) {
            paddle.stopMoving()
        }
        return false
    }

    override fun keyTyped(character: Char) = false
    override fun mouseMoved(screenX: Int, screenY: Int) = false
    override fun scrolled(amountX: Float, amountY: Float) = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
}