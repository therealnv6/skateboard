package io.github.devrawr.scoreboards

import org.bukkit.entity.Player

interface ScoreboardUpdater
{
    /**
     * Update a specific line on the scoreboard
     *
     * @param player the player to send the specific line to
     * @param line   the line index to update
     * @param text   the text to display at the line
     */
    fun updateLine(player: Player, line: Int, text: String)

    /**
     * Remove a line at a specific index
     *
     * @param player the player's scoreboard to update
     * @param line   the index of the line to remove
     */
    fun removeLine(player: Player, line: Int)
}