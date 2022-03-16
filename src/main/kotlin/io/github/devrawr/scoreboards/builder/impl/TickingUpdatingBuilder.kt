package io.github.devrawr.scoreboards.builder.impl

import io.github.devrawr.scoreboards.ScoreboardEntry
import io.github.devrawr.scoreboards.builder.UpdatingBuilder

typealias TickHandle = () -> String
typealias TickHideHandle = () -> Boolean

class TickingUpdatingBuilder(
    entry: ScoreboardEntry
) : UpdatingBuilder<TickHandle, TickHideHandle>(entry)
{
    var cooldown = 20L

    fun cooldown(duration: Long): TickingUpdatingBuilder
    {
        return this.also {
            this.cooldown = duration
        }
    }

    override fun create()
    {
        if (this.handle !is TickHandle || this.hide !is TickHideHandle)
        {
            return
        }

        val handle: TickHandle = this.handle ?: { "" }
        val hide: TickHideHandle = this.hide ?: { false }

        entry
            .updater()
            .updateRepeating(this.cooldown, handle, hide)
    }
}