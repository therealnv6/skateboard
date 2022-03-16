package io.github.devrawr.scoreboards.impl

import io.github.devrawr.scoreboards.ScoreboardUpdater
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

object DefaultScoreboardUpdater : ScoreboardUpdater
{
    private val scoreboardManager = Bukkit.getScoreboardManager()

    override fun updateLine(player: Player, line: Int, text: String)
    {
        val scoreboard = player.retrieveScoreboard()
        val objective = scoreboard.retrieveObjective()

        val display = this.splitText(text)
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

    override fun updateTitle(player: Player, title: String)
    {
        val scoreboard = player.retrieveScoreboard()
        val objective = scoreboard.retrieveObjective()

        objective.displayName = title
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

    private fun Scoreboard.retrieveObjective(): Objective
    {
        var objective = this.getObjective("kt-board")

        if (objective == null)
        {
            objective = this.registerNewObjective("kt-board", "dummy").apply {
                this.displaySlot = DisplaySlot.SIDEBAR
            }
        }

        return objective
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