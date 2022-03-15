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
}