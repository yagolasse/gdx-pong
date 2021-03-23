package com.example.pong

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.example.pong.model.Ball
import com.example.pong.model.Paddle

val SCREEN_WIDTH = Gdx.graphics.width
val SCREEN_HEIGHT = Gdx.graphics.height

class PongGame : ApplicationAdapter() {

    private val ballResPath = "ball.png"
    private val paddleResPath = "paddle.png"

    private lateinit var ball: Ball
    private lateinit var stage: Stage
    private lateinit var leftPaddle: Paddle
    private lateinit var rightPaddle: Paddle
    private lateinit var topBarrier: Rectangle
    private lateinit var leftBarrier: Rectangle
    private lateinit var bottomBarrier: Rectangle
    private lateinit var rightBarrier: Rectangle

    override fun create() {
        initializePaddles()
        initializeBall()
        initializeStage()
        initializeBarriers()

        Gdx.input.inputProcessor = InputMultiplexer(leftPaddle, rightPaddle)
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        with(ball) {
            handleCollisionWithLeftPaddle(leftPaddle.boundingRectangle)
            handleCollisionWithRightPaddle(rightPaddle.boundingRectangle)
            handleCollisionWithVerticalBarriers(topBarrier)
            handleCollisionWithVerticalBarriers(bottomBarrier)
            handleCollisionWithHorizontalBarriers(leftBarrier)
            handleCollisionWithHorizontalBarriers(rightBarrier)
        }
    }

    override fun dispose() {
        stage.dispose()
        leftPaddle.dispose()
        rightPaddle.dispose()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        // TODO implement resize screen
    }

    private fun initializePaddles() {
        val leftPaddleX = SCREEN_WIDTH * 0.1f
        val rightPaddleX = SCREEN_WIDTH * 0.9f
        val paddleY = SCREEN_HEIGHT * 0.5f

        leftPaddle = Paddle(
            leftPaddleX,
            paddleY,
            Texture(paddleResPath),
            Pair(Input.Keys.W, Input.Keys.S)
        )

        rightPaddle = Paddle(
            rightPaddleX,
            paddleY,
            Texture(paddleResPath),
            Pair(Input.Keys.UP, Input.Keys.DOWN)
        )
    }

    private fun initializeBall() {
        val x = SCREEN_WIDTH * 0.5f
        val y = SCREEN_HEIGHT * 0.5f

        ball = Ball(x, y, Texture(ballResPath))
    }

    private fun initializeStage() {
        stage = Stage(ScreenViewport()).apply {
            addActor(ball)
            addActor(leftPaddle)
            addActor(rightPaddle)
        }
    }

    private fun initializeBarriers() {
        val verticalBarriersWidth = SCREEN_WIDTH * 0.9f
        val left = SCREEN_WIDTH * 0.05f
        val right = SCREEN_WIDTH * 0.95f
        val barrierThickness = 1f

        topBarrier = Rectangle(left, SCREEN_HEIGHT.toFloat(), verticalBarriersWidth, barrierThickness)
        leftBarrier = Rectangle(left, 0f, barrierThickness, SCREEN_HEIGHT.toFloat())
        bottomBarrier = Rectangle(left, 0f, verticalBarriersWidth, barrierThickness)
        rightBarrier = Rectangle(right, 0f, barrierThickness, SCREEN_HEIGHT.toFloat())
    }
}
