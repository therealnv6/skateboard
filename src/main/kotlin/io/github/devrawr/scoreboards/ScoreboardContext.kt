package io.github.devrawr.scoreboards

import io.github.devrawr.scoreboards.builder.impl.ListenerUpdatingBuilder
import io.github.devrawr.scoreboards.builder.impl.TickingUpdatingBuilder
import io.github.devrawr.scoreboards.order.LineOrder
import io.github.devrawr.scoreboards.updating.impl.TitleUpdater
import org.bukkit.entity.Player
import org.bukkit.event.Event

class ScoreboardContext(val player: Player)
{
    val entries = mutableListOf<ScoreboardEntry>()
    var lineOrder = LineOrder.Decrement

    fun add(line: String) = this.add(entries.size, line)
    fun add(delay: Long = 20L, line: () -> String) = this.add(entries.size, delay, line)

    /**
     * Create a new [TitleUpdater] instance.
     *
     * This class can be used to update the title accordingly,
     * using things such as [TitleUpdater.listenTo] or [TitleUpdater.updateRepeating]
     *
     * @param title the default title string to begin with
     * @return a builder for ticking the title
     */
    fun title(title: String = ""): TitleUpdater
    {
        return TitleUpdater(this, title).also {
            this.updateTitle(this.player, title)
        }
    }

    /**
     * Create a new [TickingUpdatingBuilder] instance.
     *
     * Calls [ScoreboardContext.repeating], using the
     * [entries] field size as index
     *
     * @return a builder for ticking updates, new object.
     */
    fun repeating(): TickingUpdatingBuilder = this.repeating(entries.size)

    /**
     * Create a new [TickingUpdatingBuilder] instance.
     *
     * Generally used for easier generation of
     * tick-based updating entries, but in a
     * much (subjectively) easier and cleaner way to
     * invoke the methods.
     *
     * [TickingUpdatingBuilder] is used for entries
     * which should update periodically after a certain
     * amount of time, which should be specified using
     * the provided [TickingUpdatingBuilder.cooldown] method.
     *
     * @param index the index where the line will be displayed
     * @return a builder for ticking updates, new object.
     */
    fun repeating(index: Int): TickingUpdatingBuilder
    {
        val entry = ScoreboardEntry(
            this.player, "", this, index
        )

        return TickingUpdatingBuilder(entry).also {
            this.entries.add(entry)
        }
    }

    /**
     * Create a new [ListenerUpdatingBuilder] instance.
     *
     * Calls [ScoreboardContext.listen], using the
     * [entries] field size as index
     *
     * @param type the type of the event to listen to
     * @return a builder for listener updates, new object.
     */
    fun <T : Event> listen(type: Class<T>) = this.listen(entries.size, type)

    /**
     * Add a line which updates everytime an event is called for the player.
     *
     * Wrapper method for [ScoreboardContext.add], using
     * the [entries] field to find the index.
     *
     * @param line  the body to invoke everytime to get the string from
     * @return a new entry instance
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
            context = this,
            player = this.player,
            index = this.lineOrder.getRelativePosition(index),
            line = ""
        )

        return ListenerUpdatingBuilder(type, entry).also {
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
            context = this,
            player = this.player,
            index = this.lineOrder.getRelativePosition(index),
            line = line
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
            context = this,
            player = this.player,
            index = this.lineOrder.getRelativePosition(index),
            line = line.invoke(),
        ).apply {
            entries.add(
                index, this.also {
                    this
                        .updater()
                        .updateRepeating(delay, line)
                }
            )
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
            context = this,
            player = this.player,
            index = this.lineOrder.getRelativePosition(index),
            line = ""
        ).apply {
            entries.add(
                index, this.also {
                    this
                        .updater()
                        .listenTo(line)
                }
            )
        }
    }

    /**
     * Display a [ScoreboardEntry] at a certain index
     * of the scoreboard.
     *
     * This method will automatically move every entry
     * at the same [index] of the current entry up by `1`.
     *
     * @param index the index where to put the entry at
     * @param entry the entry to display on the scoreboard
     */
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

    fun updateTitle(player: Player, title: String)
    {
        Scoreboards
            .internal
            .updateTitle(
                player, title
            )
    }

    /**
     * Display a line at a certain index
     *
     * @param index the index to display the line at
     * @param line  the text to display at the index
     */
    fun displayAt(
        index: Int,
        line: String
    )
    {
        Scoreboards
            .internal
            .updateLine(
                this.player, index, line
            )
    }

    fun removeAt(index: Int)
    {
        Scoreboards
            .internal
            .removeLine(
                this.player, index
            )
    }
}