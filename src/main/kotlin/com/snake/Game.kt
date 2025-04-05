package com.snake

import GameData
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class Game : Application() {

    companion object ScreenDetails {
        private const val WIDTH = 512
        private const val HEIGHT = 512
        private const val BLOCK_SIZE = 20
    }

    private lateinit var mainStage: Stage
    private lateinit var graphicsContext: GraphicsContext
    private lateinit var mainScene: Scene

    private var snake = Snake()
    var food = Pair(0, 0)
    private var lastUpdateTime = 0L
    private var gamePaused = false
    private var gameLoop: AnimationTimer? = null
    private var score = 0

    override fun start(stage: Stage) {
        mainStage = stage
        mainStage.title="Snake"

        showMainMenu(WIDTH, HEIGHT, mainStage, this)
    }

    fun initializeGame(){
        snake.initialize(WIDTH / (2 * BLOCK_SIZE), HEIGHT / (2 * BLOCK_SIZE))
        lastUpdateTime = 0L
        spawnFood()
        snake.direction = Direction.RIGHT
        gamePaused = false
        score = 0
    }

    fun startGame(){
        val root = Group()
        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        root.children.add(canvas)

        mainScene = Scene(root)
        mainStage.scene = mainScene
        graphicsContext = canvas.graphicsContext2D

        prepareActionHandlers()

        //Game loop
        gameLoop = object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                if (gamePaused){
                    showPauseMenu(graphicsContext, WIDTH, HEIGHT, this@Game)
                    return
                }
                if (!snake.alive) {
                    deleteSaveFile()
                    this.stop()
                    showMainMenu(HEIGHT, WIDTH, mainStage, this@Game)
                } else {
                    if (::graphicsContext.isInitialized) {
                        tickAndRender(currentNanoTime)
                    } else {
                        println("GraphicsContext not initialized!")
                    }
                }
            }
        }
        gameLoop?.start()

    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler {
            event ->
            when(event.code){
                KeyCode.ESCAPE -> gamePaused = !gamePaused
                KeyCode.S -> if(gamePaused) saveGame()
                else -> if (!gamePaused) snake.changeDirection(event.code)
            }

        }
    }

    private fun tickAndRender (currentNanoTime: Long){
        val elapsedMillis = (currentNanoTime - lastUpdateTime) / 1_000_000

        if (elapsedMillis >= snake.speed) {
            snake.updateSnake(WIDTH, HEIGHT, BLOCK_SIZE, this)
            lastUpdateTime = currentNanoTime
        }

        renderGame()
    }

    private fun renderGame(){
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())
        graphicsContext.fill = Color.BLACK
        graphicsContext.fillRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        graphicsContext.fill = Color.BLUE
        snake.body.forEach { part ->
            graphicsContext.fillRect(
                (part.first * BLOCK_SIZE).toDouble(),
                (part.second * BLOCK_SIZE).toDouble(),
                BLOCK_SIZE.toDouble(),
                BLOCK_SIZE.toDouble()
            )
        }

        graphicsContext.fill = Color.YELLOW
        graphicsContext.fillRect(
            (food.first * BLOCK_SIZE).toDouble(),
            (food.second * BLOCK_SIZE).toDouble(),
            BLOCK_SIZE.toDouble(),
            BLOCK_SIZE.toDouble()
        )

        graphicsContext.fill = Color.WHITE
        graphicsContext.fillText("Score: $score", 10.0, 20.0)
    }
    fun spawnFood() {
        val width = WIDTH / BLOCK_SIZE
        val height = HEIGHT / BLOCK_SIZE
        var newFood: Pair<Int, Int>
        do {
            newFood = Pair((0 until width).random(), (0 until height).random())
        } while (newFood in snake.body)

        food = newFood
        score++
    }

    private fun saveGame(){
        try {
            val gameData = GameData(
                snakeBody = snake.body,
                foodPosition = food,
                snakeDirection = snake.direction.toString(),
                alive = snake.alive,
                speed = snake.speed,
                score = score
            )
            ObjectOutputStream(FileOutputStream("game_save.dat")).use { it.writeObject(gameData) }
        } catch (e: Exception) {
            println("Error: " + e.message)
        }
        showMainMenu(HEIGHT, WIDTH, mainStage, this)
    }

    fun loadGame() {
        try {
            ObjectInputStream(FileInputStream("game_save.dat")).use {
                val gameData = it.readObject() as GameData
                snake.body = gameData.snakeBody.toMutableList()
                food = gameData.foodPosition
                snake.setDirection(gameData.snakeDirection)
                snake.alive = gameData.alive
                snake.speed = gameData.speed
                score = gameData.score
                gamePaused = false
                startGame()
            }
        } catch (e: Exception) {
            println("Error: " + e.message)
        }
    }

    private fun deleteSaveFile() {
        val saveFile = java.io.File("game_save.dat")
        if (saveFile.exists()) {
            if (saveFile.delete()) {
                println("Save deleted.")
            } else {
                println("Error, couldn't delete save.")
            }
        }
    }



}
