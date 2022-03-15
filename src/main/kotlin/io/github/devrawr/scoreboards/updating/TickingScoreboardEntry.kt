package io.github.devrawr.scoreboards.updating

import io.github.devrawr.scoreboards.ScoreboardContext
import io.github.devrawr.scoreboards.ScoreboardEntry
import io.github.devrawr.tasks.Tasks
import org.bukkit.entity.Player

class TickingScoreboardEntry(
    player: Player,
    line: String,
    index: Int,
    delay: Long,
    context: ScoreboardContext,
    invoke: () -> String
) : ScoreboardEntry(player, line, context, index)
{
    init
    {
        Tasks
            .async()
            .repeating(0L, delay) {
                this.display(invoke.invoke())
            }
    }
}