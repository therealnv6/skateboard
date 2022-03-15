package io.github.devrawr.scoreboards

import org.bukkit.entity.Player

open class ScoreboardEntry(
    open val player: Player,
    open var line: String,
    open val context: ScoreboardContext,
    open val index: Int,
)
{
    fun display(line: String)
    {
        if (this.line != line)
        {
            this.line = line
        }

        this.context.displayAt(this.index, this.line)
    }
}
