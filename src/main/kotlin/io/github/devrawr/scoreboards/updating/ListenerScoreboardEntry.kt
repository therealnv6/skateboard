package io.github.devrawr.scoreboards.updating

import io.github.devrawr.events.Events
import io.github.devrawr.scoreboards.ScoreboardContext
import io.github.devrawr.scoreboards.ScoreboardEntry
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent

class ListenerScoreboardEntry<T : Event>(
    player: Player, line: String, index: Int, type: Class<T>, context: ScoreboardContext, invoke: (T) -> String
) : ScoreboardEntry(
    player, line, context, index
)
{
    var removeIf: () -> Boolean = {
        false
    }

    var removeIfEvent: (T) -> Boolean = {
        false
    }

    init
    {
        Events
            .listenTo(type)
            .filter {
                (it is PlayerEvent && it.player == this.player) || it !is PlayerEvent
            }
            .on {
                val remove = this.removeIf.invoke() || this.removeIfEvent.invoke(it)

                if (remove)
                {
                    this.remove()
                } else
                {
                    this.display(invoke.invoke(it))
                }
            }
    }

    override fun hideIf(bool: () -> Boolean)
    {
        this.removeIf = bool
    }

    fun eventHideIf(bool: (T) -> Boolean)
    {
        this.removeIfEvent = bool
    }
}