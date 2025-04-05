package com.snake

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Stage
import javafx.geometry.Pos
import javafx.scene.layout.*

fun showMainMenu(height: Int, width: Int, stage: Stage, game : Game){
    val root = VBox(10.0)
    root.alignment = Pos.CENTER

    val menuScene = Scene(root, width.toDouble(), height.toDouble())

    val startButton = Button("Start Game")
    startButton.onAction = EventHandler {
        game.initializeGame()
        game.startGame()
    }

    val saveFile = java.io.File("game_save.dat")

    val loadButton = Button("Load Game")
    loadButton.onAction = EventHandler {
        game.loadGame()
    }
    loadButton.isDisable = !saveFile.exists()

    root.children.addAll(startButton, loadButton)

    stage.scene = menuScene
    stage.show()

}