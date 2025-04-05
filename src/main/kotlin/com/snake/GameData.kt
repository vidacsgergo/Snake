import java.io.Serializable

data class GameData(
    val snakeBody: List<Pair<Int, Int>>,
    val foodPosition: Pair<Int, Int>,
    val snakeDirection: String,
    val alive: Boolean,
    val speed: Long,
    val score: Int
) : Serializable
