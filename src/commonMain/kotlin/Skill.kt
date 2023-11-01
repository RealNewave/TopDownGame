import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.time.*

abstract class Skill(skillName: String, color: RGBA, val key: String) : Container() {
    private val view = solidRect(50, 50, color)
    private val skillTimer = Text("", color = Colors.WHEAT)
    private val skillKey = Text(key)
    private val skillNameTag = Text(skillName)
    private var timeRemaining = 0
    var timeSpan = 1
    open var isUsingSkill = false
    open var canUseSkill = true
    init {
        addChildren(listOf(view, skillTimer, skillKey, skillNameTag))
        skillTimer.centerOn(view)
        skillKey.xy(5, 5)
        skillNameTag.alignTopToBottomOf(view, 5)
        addFixedUpdater(timesPerSecond = 1.timesPerSecond, initial = false) {
            if(timeRemaining <= 0){
                canUseSkill = true
                updateSkillTimer("")
                return@addFixedUpdater
            }
            skillTimer.setText("$timeRemaining")
            timeRemaining--
        }
    }

    abstract fun useSkill(sprite: Sprite, direction: String)

    fun startCoolDown(seconds: Int) {
        timeRemaining = seconds
        timeSpan = 1
    }

    private fun updateSkillTimer(text: String) {
        skillTimer.setText(text)
    }

}

class Roll(private val rollSprites: Map<String, SpriteAnimation>, key: String) : Skill("Roll", Colors.ORANGERED, key) {
    private val coolDown = 3

    override fun useSkill(sprite: Sprite, direction: String) {
        rollSprites["Roll$direction"]?.let {
            sprite.playAnimation(it, 100.milliseconds)
            startCoolDown(coolDown)
            canUseSkill = false
            isUsingSkill = true
            sprite.onAnimationCompleted {
                isUsingSkill = false
                player.q = false

            }
        }
    }
}
