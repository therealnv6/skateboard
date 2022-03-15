package io.github.devrawr.scoreboards.updating

import io.github.devrawr.scoreboards.ScoreboardContext
import io.github.devrawr.scoreboards.ScoreboardEntry
import io.github.devrawr.tasks.Tasks
import org.bukkit.entity.Player

class TickingScoreboardEntry(
    player: Player, line: String, index: Int, delay: Long, context: ScoreboardContext, invoke: () -> String
) : ScoreboardEntry(
    player, line, context, index
)
{
    var removeIf: () -> Boolean = {
        false
    }

    init
    {
        Tasks
            .async()
            .repeating(0L, delay) {
                val remove = removeIf.invoke()

                if (remove)
                {
                    this.remove()
                } else
                {
                    this.display(invoke.invoke())
                }
            }
    }

    override fun hideIf(bool: () -> Boolean)
    {
        this.removeIf = bool
    }
}