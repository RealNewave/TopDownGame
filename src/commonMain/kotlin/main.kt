import korlibs.event.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.time.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

lateinit var player: Player

suspend fun main() = Korge(
    virtualSize = Size(1440, 900),
    title = "TopDownGame",
    bgcolor = RGBA(253, 247, 240),
    /**
    `gameId` is associated with the location of storage, which contains `history` and `best`.
    see [Views.realSettingsFolder]
     */
    gameId = "com.devex.topdowngame",
    forceRenderEveryFrame = false, // Optimization to reduce battery usage!
) {
    val directions = listOf("Down", "DownLeft", "Left", "UpLeft", "Up", "UpRight", "Right", "DownRight")
    val directionSprites = directions.associateWith {
        getAnimation(it)
    }
    val rolls = directions.map { "Roll$it" }
    val rollSprites = rolls.associateWith { getAnimation(it) }


    val roll = Roll(rollSprites, Key.Q.name)
    val skills = listOf(roll)


    skills.forEachIndexed { index, skill ->
        stage.addChild(skill)
        skill.alignLeftToLeftOf(stage, 20 + index * 50 )
        skill.alignBottomToBottomOf(stage, 20)
    }

    player = Player(directionSprites, skills)
    stage.addChild(player)
    player.centerOn(stage)


    addFixedUpdater(30.timesPerSecond) {
        handleKeyEvents(player)
        player.moveInDirection()
    }
}


private fun Stage.handleKeyEvents(player: Player) {
    keys {
        justDown(Key.Q) {
            player.activateQ()
        }
    }
    fun Key.isPressed(): Boolean = input.keys[this]

    if (Key.UP.isPressed()) {
        player.direction = "Up"
        if (Key.RIGHT.isPressed()) player.direction += "Right"
        if (Key.LEFT.isPressed()) player.direction += "Left"
    } else if (Key.DOWN.isPressed()) {
        player.direction = "Down"
        if (Key.RIGHT.isPressed()) player.direction += "Right"
        else if (Key.LEFT.isPressed()) player.direction += "Left"
    } else if (Key.LEFT.isPressed()) player.direction = "Left"
    else if (Key.RIGHT.isPressed()) player.direction = "Right"
}

suspend fun getAnimation(direction: String): SpriteAnimation {
    val spriteMap = resourcesVfs["animations/character/Character_$direction.png"].readBitmap()

    return SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 32,
        spriteHeight = 32,
        columns = 4,
        rows = 1,
        numberOfFrames = 4,
    )
}
