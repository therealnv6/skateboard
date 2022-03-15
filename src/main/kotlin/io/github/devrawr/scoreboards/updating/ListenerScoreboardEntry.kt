package io.github.devrawr.scoreboards.updating

import io.github.devrawr.events.Events
import io.github.devrawr.scoreboards.ScoreboardEntry
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent

class ListenerScoreboardEntry<T : Event>(
    player: Player,
    line: String,

    type: Class<T>,
    invoke: (T) -> String
) : ScoreboardEntry(player, line)
{
    init
    {
        Events
            .listenTo(type)
            .filter {
                (it is PlayerEvent && it.player == this.player) || it !is PlayerEvent
            }
            .on {
                this.line = invoke.invoke(it)
            }
    }
}