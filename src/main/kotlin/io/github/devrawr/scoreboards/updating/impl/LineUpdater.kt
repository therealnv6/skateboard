package io.github.devrawr.scoreboards.updating.impl

import io.github.devrawr.scoreboards.ScoreboardEntry
import io.github.devrawr.scoreboards.updating.Updater

class LineUpdater(private val entry: ScoreboardEntry) : Updater()
{
    override val player = entry.player

    override fun update(line: String)
    {
        this.entry.display(line)
    }

    override fun remove()
    {
        this.entry.remove()
    }
}