package io.github.devrawr.scoreboards.updating

import io.github.devrawr.events.Events
import io.github.devrawr.scoreboards.ScoreboardContext
import io.github.devrawr.scoreboards.ScoreboardEntry
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent

class ListenerScoreboardEntry<T : Event>(
    player: Player,
    line: String,
    index: Int,

    type: Class<T>,
    context: ScoreboardContext,
    invoke: (T) -> String
) : ScoreboardEntry(player, line, context, index)
{
    init
    {
        Events
            .listenTo(type)
            .filter {
                (it is PlayerEvent && it.player == this.player) || it !is PlayerEvent
            }
            .on {
                this.display(invoke.invoke(it))
            }
    }
}