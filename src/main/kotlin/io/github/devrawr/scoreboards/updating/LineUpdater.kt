package io.github.devrawr.scoreboards.updating

import io.github.devrawr.events.Events
import io.github.devrawr.scoreboards.ScoreboardEntry
import io.github.devrawr.tasks.Tasks
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent

// TODO: 3/16/22 add kdocs, kind of lazy.
class LineUpdater(private val entry: ScoreboardEntry)
{
    inline fun <reified T : Event> listenTo(noinline update: (T) -> String): LineUpdater =
        this.listenTo(T::class.java, update)

    inline fun <reified T : Event> listenTo(
        noinline update: (T) -> String,
        noinline hideOn: (T) -> Boolean
    ): LineUpdater = this.listenTo(T::class.java, update, hideOn)

    fun <T : Event> listenTo(type: Class<T>, update: (T) -> String): LineUpdater
    {
        return this.also {
            this.listenTo(
                type,
                update
            ) {
                false
            }
        }
    }

    fun <T : Event> listenTo(type: Class<T>, update: (T) -> String, hideOn: (T) -> Boolean): LineUpdater
    {
        return this.also {
            Events
                .listenTo(type)
                .filter { (it is PlayerEvent && it.player == entry.player) || it !is PlayerEvent }
                .on {
                    if (hideOn.invoke(it))
                    {
                        this.remove()
                    } else
                    {
                        this.update(
                            update.invoke(it)
                        )
                    }
                }
        }
    }

    fun updateRepeating(duration: Long, update: () -> String): LineUpdater
    {
        return this.also {
            this.updateRepeating(duration, update) {
                false
            }
        }
    }

    fun updateRepeating(duration: Long, update: () -> String, cancelOn: () -> Boolean): LineUpdater
    {
        return this.also {
            Tasks
                .async()
                .repeating(0L, duration) {
                    if (cancelOn.invoke())
                    {
                        this.remove()
                    } else
                    {
                        this.update(
                            update.invoke()
                        )
                    }
                }
        }
    }

    fun updateAfter(duration: Long, update: () -> String): LineUpdater
    {
        return this.also {
            this.updateAfter(duration, update) {
                false
            }
        }
    }

    fun updateAfter(duration: Long, update: () -> String, cancelOn: () -> Boolean): LineUpdater
    {
        return this.also {
            Tasks
                .async()
                .delay(duration) {
                    if (cancelOn.invoke())
                    {
                        this.remove()
                    } else
                    {
                        this.update(
                            update.invoke()
                        )
                    }
                }
        }
    }

    fun update(line: String)
    {
        this.entry.display(line)
    }

    fun remove()
    {
        this.entry.remove()
    }
}