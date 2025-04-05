package com.snake

import javafx.scene.input.KeyCode

class Snake(){
    var direction = Direction.RIGHT
    var body = mutableListOf<Pair<Int, Int>>()
    var speed = 200L
    var alive = false;

    fun initialize(width: Int, height : Int){
        alive = true
        body.clear()
        for (i in 0..3){
            body.add(Pair(width, height))
        }
    }

    fun changeDirection(keyCode : KeyCode){
        when (keyCode){
            KeyCode.UP -> if (direction != Direction.DOWN) direction = Direction.UP
            KeyCode.DOWN -> if (direction != Direction.UP) direction = Direction.DOWN
            KeyCode.LEFT -> if (direction != Direction.RIGHT) direction = Direction.LEFT
            KeyCode.RIGHT -> if (direction != Direction.LEFT) direction = Direction.RIGHT
            else -> {direction}
        }
    }

    fun updateSnake(width: Int, height: Int, blockSize: Int, game: Game){
        val head = body.first()
        var newHead = when(direction){
            Direction.UP -> Pair(head.first, head.second-1)
            Direction.DOWN -> Pair(head.first, head.second + 1)
            Direction.RIGHT -> Pair(head.first + 1, head.second)
            Direction.LEFT -> Pair(head.first - 1, head.second)
        }

        newHead = Pair(
            (newHead.first + (width / blockSize)) % (width / blockSize),
            (newHead.second + (height / blockSize)) % (height / blockSize)
        )

        if (newHead in body){
            alive = false
            return
        }

        body.add(0, newHead)

        if (newHead == game.food){
            game.spawnFood()
            if (speed > 50) speed -= 5
        }else{
            body.removeLast()
        }
    }
    fun setDirection(direction: String) {
        this.direction = Direction.valueOf(direction)
    }
}