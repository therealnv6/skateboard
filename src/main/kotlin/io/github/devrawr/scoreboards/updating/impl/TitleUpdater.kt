package io.github.devrawr.scoreboards.updating.impl

import io.github.devrawr.scoreboards.ScoreboardContext
import io.github.devrawr.scoreboards.updating.Updater

class TitleUpdater(
    private val context: ScoreboardContext,
    private val line: String
) : Updater()
{
    override val player = context.player

    init
    {
        this.update(this.line)
    }

    override fun update(line: String)
    {
        this.context.updateTitle(player, line)
    }

    override fun remove()
    {
    }
}