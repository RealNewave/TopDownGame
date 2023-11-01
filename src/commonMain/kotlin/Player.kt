import korlibs.korge.view.*
import korlibs.time.*

class Player(
    val directionSprites: Map<String, SpriteAnimation>,
    val skills: List<Skill>
) : Container() {

    private val sprite = sprite(directionSprites.values.first())
    var direction = ""
    var q = false

    init {
        this.size(100, 100)
    }

    fun moveInDirection() {
        if (q) {
            skills.find { it.key == "Q" }?.let {
                if (it.isUsingSkill && !it.canUseSkill) return
                it.useSkill(sprite, direction)
            }
        } else
            directionSprites[direction]?.let {
                sprite.playAnimation(it, 250.milliseconds)
            }
        direction = ""
    }

    fun activateQ(){
        q = true
    }

}
