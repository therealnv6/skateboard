package io.github.devrawr.scoreboards

import io.github.devrawr.scoreboards.builder.impl.ListenerUpdatingBuilder
import org.bukkit.entity.Player
import org.bukkit.event.Event

class ScoreboardContext(val player: Player)
{
    val entries = mutableListOf<ScoreboardEntry>()

    fun add(line: String) = this.add(entries.size, line)
    fun add(delay: Long = 20L, line: () -> String) = this.add(entries.size, delay, line)

    /**
     * Create a new [ListenerUpdatingBuilder] instance.
     *
     * Calls [ScoreboardContext.listen], using the
     * [entries] field size as index
     *
     * @param type the type of the event to listen to
     */
    fun <T : Event> listen(type: Class<T>) = this.listen(entries.size, type)

    /**
     * Add a line which updates everytime an event is called for the player.
     *
     * Wrapper method for [ScoreboardContext.add], using
     * the [entries] field to find the index.
     *
     * @param line  the body to invoke everytime to get the string from
     */
    inline fun <reified T : Event> add(noinline line: (T) -> String) = this.add(entries.size, line)

    /**
     * Create a new [ListenerUpdatingBuilder] instance.
     *
     * Calls [ScoreboardContext.listen], using:
     * - Provided generic type [T] as the 'type' parameter.
     * - [entries] size as index
     *
     * @return a builder for listener updates, new object.
     * @see [ScoreboardContext.listen]
     */
    inline fun <reified T : Event> listen(): ListenerUpdatingBuilder<T> = this.listen(entries.size, T::class.java)

    /**
     * Create a new [ListenerUpdatingBuilder] instance.
     *
     * Calls [ScoreboardContext.listen], using the
     * provided generic type [T] as the 'type' parameter.
     *
     * @param index the index where the line will be displayed
     * @return a builder for listener updates, new object.
     * @see [ScoreboardContext.listen]
     */
    inline fun <reified T : Event> listen(index: Int): ListenerUpdatingBuilder<T> = this.listen(index, T::class.java)

    /**
     * Create a new [ListenerUpdatingBuilder] instance.
     *
     * Generally used for easier generation of
     * listener-based updating entries, but in a
     * much (subjectively) easier and cleaner way to
     * invoke the methods.
     *
     * @param index the index where the line will be displayed
     * @param type  the event to listen to
     * @return a builder for listener updates, new object.
     */
    fun <T : Event> listen(index: Int, type: Class<T>): ListenerUpdatingBuilder<T>
    {
        val entry = ScoreboardEntry(
            this.player, "", this, index
        )

        return ListenerUpdatingBuilder(
            type, entry
        ).also {
            entries.add(index, entry)
        }
    }

    /**
     * Add a static line to the scoreboard
     *
     * @param index the index where the line will be displayed
     * @param line  the line to display at the slot
     * @return the created entry
     */
    fun add(index: Int, line: String): ScoreboardEntry
    {
        return ScoreboardEntry(
            this.player, line, this, index,
        ).also {
            this.displayAt(
                index, it
            )
        }
    }

    /**
     * Add a line which updates everytime an event is called for the player
     *
     * @param index the index where the line will be displayed
     * @param delay the delay between the periodical updates
     * @param line  the body to invoke everytime to get the string from
     * @return the created entry
     */
    fun add(index: Int, delay: Long = 20L, line: () -> String): ScoreboardEntry
    {
        return ScoreboardEntry(
            this.player, line.invoke(), this, index
        ).apply {
            entries.add(index, this)

            this
                .updater()
                .updateRepeating(delay, line)
        }
    }

    /**
     * Add a line everytime an event is called for the player
     *
     * @param index the index where the line will be displayed
     * @param line  the body to invoke everytime to get the string from
     * @return the created entry
     */
    inline fun <reified T : Event> add(
        index: Int,
        noinline line: (T) -> String
    ): ScoreboardEntry
    {
        return ScoreboardEntry(
            this.player, "", this, index
        ).apply {
            entries.add(index, this)

            this
                .updater()
                .listenTo(line)
        }
    }

    private fun displayAt(index: Int, entry: ScoreboardEntry)
    {
        if (this.entries.size >= index - 1)
        {
            for (key in this.entries.withIndex())
            {
                if (key.value.index == -1)
                {
                    this.displayAt(index + 1, entry)
                    break
                }

                if (key.index >= index)
                {
                    this.entries.removeAt(key.index)
                    this.entries.add(index, key.value)

                    this.displayAt(index + 1, key.value)
                }
            }
        }

        this.entries.add(index, entry)
        this.displayAt(index, entry.line)
    }

    /**
     * Display a line at a certain index
     *
     * @param index the index to display the line at
     * @param line  the text to display at the index
     */
    fun displayAt(index: Int, line: String)
    {
        Scoreboards.updater.updateLine(
            this.player, index, line
        )
    }

    fun removeAt(index: Int)
    {
        Scoreboards.updater.removeLine(
            this.player, index
        )
    }
}