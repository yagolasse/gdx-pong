package com.example.pong.pool

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.badlogic.gdx.utils.Pool

class RepeatActionPool(
    private val actionPool: Pool<out Action>
) : Pool<RepeatAction>() {

    override fun obtain(): RepeatAction? {
        return super.obtain()?.apply {
            action = actionPool.obtain()
        }
    }

    override fun newObject(): RepeatAction {
        return Actions.action(RepeatAction::class.java).apply {
            count = RepeatAction.FOREVER
        }
    }
}