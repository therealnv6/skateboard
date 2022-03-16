package io.github.devrawr.scoreboards.builder.impl

import io.github.devrawr.scoreboards.ScoreboardEntry
import io.github.devrawr.scoreboards.builder.UpdatingBuilder
import org.bukkit.event.Event

typealias ListenerHandle<T> = (T) -> String
typealias ListenerHideHandle<T> = (T) -> Boolean

class ListenerUpdatingBuilder<T : Event>(
    private val type: Class<T>,
    entry: ScoreboardEntry
) : UpdatingBuilder<ListenerHandle<T>, ListenerHideHandle<T>>(entry)
{
    override fun create()
    {
        if (this.handle !is ListenerHandle<*> || this.hide !is ListenerHideHandle<*>)
        {
            return
        }

        val handle: ListenerHandle<T> = this.handle ?: { "" }
        val hide: ListenerHideHandle<T> = this.hide ?: { false }

        entry
            .updater()
            .listenTo(
                this.type, handle, hide
            )
    }
}