package io.github.devrawr.scoreboards.updating

import io.github.devrawr.scoreboards.ScoreboardEntry
import io.github.devrawr.tasks.Tasks
import org.bukkit.entity.Player

class TickingScoreboardEntry(
    player: Player,
    line: String,
    delay: Long,
    invoke: () -> String
) : ScoreboardEntry(player, line)
{
    init
    {
        Tasks
            .async()
            .repeating(0L, delay) {
                this.line = invoke.invoke()
            }
    }
}