package io.github.devrawr.scoreboards

import io.github.devrawr.scoreboards.updating.impl.LineUpdater
import org.bukkit.entity.Player

class ScoreboardEntry(
    val player: Player,

    internal var line: String,
    private val context: ScoreboardContext,
    internal val index: Int,
)
{
    private val updater = LineUpdater(this)

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
