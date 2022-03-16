package io.github.devrawr.scoreboards.updating

import io.github.devrawr.events.Events
import io.github.devrawr.tasks.Tasks
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent

abstract class Updater
{
    abstract val player: Player

    abstract fun update(line: String)
    abstract fun remove()

    inline fun <reified T : Event> listenTo(noinline update: (T) -> String): Updater =
        this.listenTo(T::class.java, update)

    inline fun <reified T : Event> listenTo(
        noinline update: (T) -> String,
        noinline hideOn: (T) -> Boolean
    ): Updater = this.listenTo(T::class.java, update, hideOn)

    fun <T : Event> listenTo(type: Class<T>, update: (T) -> String): Updater
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

    fun <T : Event> listenTo(type: Class<T>, update: (T) -> String, hideOn: (T) -> Boolean): Updater
    {
        return this.also {
            Events
                .listenTo(type)
                .filter { (it is PlayerEvent && it.player == this.player) || it !is PlayerEvent }
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

    fun updateRepeating(duration: Long, update: () -> String): Updater
    {
        return this.also {
            this.updateRepeating(duration, update) {
                false
            }
        }
    }

    fun updateRepeating(duration: Long, update: () -> String, cancelOn: () -> Boolean): Updater
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

    fun updateAfter(duration: Long, update: () -> String): Updater
    {
        return this.also {
            this.updateAfter(duration, update) {
                false
            }
        }
    }

    fun updateAfter(duration: Long, update: () -> String, cancelOn: () -> Boolean): Updater
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
}