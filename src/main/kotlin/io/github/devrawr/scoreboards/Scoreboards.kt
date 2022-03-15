package io.github.devrawr.scoreboards

import io.github.devrawr.events.Events
import io.github.devrawr.scoreboards.impl.DefaultScoreboardUpdater
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent

object Scoreboards
{
    var updater: ScoreboardUpdater = DefaultScoreboardUpdater

    /**
     * Register a scoreboard to automatically be applied
     * whenever a player joins
     *
     * @param body the body to create the scoreboard from
     */
    fun registerOnJoin(body: (Player) -> ScoreboardContext)
    {
        this.registerOnJoin(
            Scoreboard.new(body)
        )
    }

    /**
     * Register a scoreboard to automatically be applied
     * whenever a player joins
     *
     * @param scoreboard the scoreboard to display
     */
    fun registerOnJoin(scoreboard: Scoreboard)
    {
        Events
            .listenTo<PlayerJoinEvent>()
            .on { scoreboard.initialize(it.player) }
    }
}