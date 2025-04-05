package com.snake

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

fun showPauseMenu(graphicsContext: GraphicsContext, width: Int, height: Int, game: Game) {
    graphicsContext.fill = Color.GRAY
    graphicsContext.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())

    // Show pause message
    graphicsContext.fill = Color.WHITE
    graphicsContext.fillText("Game Paused", width / 2.0 - 50, height / 2.0)
    graphicsContext.fillText("Press 'ESC' to Resume or 'S' to save and go to the Main Menu", width / 2.0 - 160, height / 2.0 + 20)
}