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
            var current = "hey"

            context.title("")
                .updateRepeating(20L) {
                    if (current.length <= 12)
                    {
                        current += "."
                    }

                    return@updateRepeating current
                }

            context.add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}----------------") // this will never update

            context.add<PlayerMoveEvent> {
                it.to.x.toString()
            } // this will only update every time the player moves

            context.add("")
                .updater()
                .listenTo<PlayerMoveEvent> {
                    it.to.x.toString()
                }
                .updateAfter(20L) {
                    "20 ticks have passed and you haven't moved"
                } // this will update both on PlayerMoveEvent and ONCE after 20 ticks

            context.add("")
                .updater()
                .updateAfter(20L) {
                    player.health.toString()
                } // same thing as the method unde

            context.add(20L) {
                player.health.toString()
            } // same thing as method above

            context.add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}----------------") // this will never update

            return@registerOnJoin context
        }
    }
}