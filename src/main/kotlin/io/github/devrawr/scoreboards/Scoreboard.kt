package io.github.devrawr.scoreboards

import org.bukkit.entity.Player

interface Scoreboard
{
    companion object
    {
        fun new(body: (Player) -> ScoreboardContext): Scoreboard
        {
            return object : Scoreboard
            {
                override fun initialize(player: Player): ScoreboardContext
                {
                    return body.invoke(player)
                }
            }
        }
    }

    /**
     * Initialize the scoreboard for the player for the first time
     *
     * @param player the player to create the scoreboard for
     * @return the context of the scoreboard, which will be used to update it
     */
    fun initialize(player: Player): ScoreboardContext
}