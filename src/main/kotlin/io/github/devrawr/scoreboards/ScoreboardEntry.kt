package io.github.devrawr.scoreboards

import org.bukkit.entity.Player

open class ScoreboardEntry(
    open val player: Player,
    open var line: String,
    open val context: ScoreboardContext,
    open val index: Int,
)
{
    open fun hideIf(bool: () -> Boolean)
    {
        if (bool.invoke())
        {
            this.remove()
        }
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
