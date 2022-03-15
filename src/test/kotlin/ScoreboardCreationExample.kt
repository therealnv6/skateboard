import io.github.devrawr.scoreboards.ScoreboardContext
import io.github.devrawr.scoreboards.Scoreboards
import org.bukkit.ChatColor
import org.bukkit.event.player.PlayerMoveEvent

class ScoreboardCreationExample
{
    fun createSimpleBoard()
    {
        Scoreboards.registerOnJoin { player ->
            val context = ScoreboardContext(player)

            context.add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}----------------")

            context.add<PlayerMoveEvent> {
                it.to.x.toString()
            }

            context.add(20L) {
                player.health.toString()
            }

            context.add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}----------------")

            return@registerOnJoin context
        }
    }
}