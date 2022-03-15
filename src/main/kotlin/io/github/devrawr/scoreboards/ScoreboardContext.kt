package io.github.devrawr.scoreboards

import io.github.devrawr.events.Events
import io.github.devrawr.tasks.Tasks
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent

class ScoreboardContext(val player: Player)
{
    val lines = mutableListOf<String>()

    fun add(line: String) = this.add(lines.size, line)
    fun add(delay: Long = 20L, line: () -> String) = this.add(lines.size, delay, line)
    inline fun <reified T : Event> add(crossinline line: (T) -> String) = this.add(lines.size, line)

    /**
     * Add a static line to the scoreboard
     *
     * @param index the index where the line will be displayed
     * @param line  the line to display at the slot
     */
    fun add(index: Int = lines.size, line: String)
    {
        lines.add(index, line)
    }

    /**
     * Add a line which updates periodically to the scoreboard.
     *
     * @param index the index where the line will be displayed
     * @param delay the delay between the periodical updates
     * @param line  the body to invoke everytime to get the string from
     */
    fun add(index: Int = lines.size, delay: Long = 20L, line: () -> String)
    {
        Tasks
            .sync()
            .repeating(0L, delay) {
                this.displayAt(index, line.invoke())
            }
    }

    /**
     * Add a line everytime an event is called for the player
     *
     * @param index the index where the line will be displayed
     * @param line  the body to invoke everytime to get the string from
     */
    inline fun <reified T : Event> add(
        index: Int = lines.size,
        crossinline line: (T) -> String
    )
    {
        Events
            .listenTo<T>()
            .filter {
                (it is PlayerEvent && it.player == this.player) || it !is PlayerEvent
            }
            .on {
                this.displayAt(index, line.invoke(it))
            }
    }

    /**
     * Display a line at a certain index
     *
     * @param index the index to display the line at
     * @param line  the text to display at the index
     */
    fun displayAt(index: Int, line: String)
    {
        this.lines.add(
            index, line
        )

        Scoreboards.updater.updateLine(
            this.player, index, line
        )
    }
}