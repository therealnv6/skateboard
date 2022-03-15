package io.github.devrawr.scoreboards

import io.github.devrawr.scoreboards.updating.LineUpdater
import org.bukkit.entity.Player

class ScoreboardEntry(
    val player: Player,
    var line: String,
    val context: ScoreboardContext,
    val index: Int,
)
{
    val updater = LineUpdater(this)

    fun updater(): LineUpdater
    {
        return this.updater
    }

    fun display(line: String)
    {
        if (this.line != line)
        {
            this.line = line
        }

        this.context.displayAt(this.index, this.line)
    }

    fun remove()
    {
        this.context.removeAt(this.index)
    }
}
