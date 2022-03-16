package io.github.devrawr.scoreboards.updating.impl

import io.github.devrawr.scoreboards.ScoreboardContext
import io.github.devrawr.scoreboards.updating.Updater

class TitleUpdater(private val context: ScoreboardContext) : Updater()
{
    override val player = context.player

    override fun update(line: String)
    {
        this.context.updateTitle(player, line)
    }

    override fun remove() {}
}