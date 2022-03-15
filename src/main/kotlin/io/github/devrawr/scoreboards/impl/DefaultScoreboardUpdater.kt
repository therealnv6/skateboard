package io.github.devrawr.scoreboards.impl

import io.github.devrawr.scoreboards.ScoreboardUpdater
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot

object DefaultScoreboardUpdater : ScoreboardUpdater
{
    private val scoreboardManager = Bukkit.getScoreboardManager()

    override fun updateLine(player: Player, line: Int, text: String)
    {
        val scoreboard = player.retrieveScoreboard()
        val display = this.splitText(text)

        var objective = scoreboard.getObjective("kt-board")

        if (objective == null)
        {
            objective = scoreboard.registerNewObjective("kt-board", "dummy").apply {
                this.displaySlot = DisplaySlot.SIDEBAR
            }
        }

        val identifier = this.getIdentifier(line)
        val team = scoreboard.getTeam(identifier)
            ?: scoreboard.registerNewTeam(identifier)

        if (!team.entries.contains(identifier))
        {
            team.addEntry(identifier)
        }

        team.prefix = display[0]
        team.suffix = display[1]

        objective
            .getScore(identifier)
            .score = line
    }

    override fun removeLine(player: Player, line: Int)
    {
        val scoreboard = player.retrieveScoreboard()
        val identifier = this.getIdentifier(line)

        var objective = scoreboard.getObjective("kt-board")

        if (objective == null)
        {
            objective = scoreboard.registerNewObjective("kt-board", "dummy").apply {
                this.displaySlot = DisplaySlot.SIDEBAR
            }
        }

        if (objective.getScore(identifier) != null)
        {
            scoreboard.resetScores(identifier)
        }
    }

    private fun getIdentifier(index: Int): String
    {
        return ChatColor.values()[index].toString() + ChatColor.WHITE;
    }

    private fun Player.retrieveScoreboard(): org.bukkit.scoreboard.Scoreboard
    {
        val scoreboard = if (this.scoreboard == scoreboardManager.mainScoreboard)
        {
            scoreboardManager.newScoreboard
        } else
        {
            this.scoreboard
        }

        this.scoreboard = scoreboard

        return scoreboard
    }

    private fun splitText(text: String): Array<String>
    {
        return if (text.length < 17)
        {
            arrayOf(text, "")
        } else
        {
            val left = text.substring(0, 16)
            val right = text.substring(16)

            if (left.endsWith("§"))
            {
                arrayOf(
                    left.substring(0, left.toCharArray().size - 1),
                    "${ChatColor.getLastColors(left)} $right"
                )
            } else
            {
                arrayOf(
                    left,
                    "${ChatColor.getLastColors(left)} $right"
                )
            }
        }
    }
}