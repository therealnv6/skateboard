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

    init
    {
        this.display(line)
    }

    /**
     * Retrieve the current [LineUpdater] instance.
     *
     * This instance is created upon
     * class initialization.
     *
     * @return [updater]
     */
    fun updater(): LineUpdater
    {
        return this.updater
    }

    /**
     * Display a line on the context's scoreboard.
     *
     * This will automatically update the
     * cached [ScoreboardEntry.line] line
     * if the lines are not identical.
     *
     * @param line the line to update the current displayed line with
     */
    fun display(line: String)
    {
        if (this.line != line)
        {
            this.line = line
        }

        this.context.displayAt(this.index, this.line)
    }

    /**
     * Completely remove the line from the scoreboard.
     */
    fun remove()
    {
        this.context.removeAt(this.index)
    }
}
