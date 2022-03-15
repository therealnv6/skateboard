package io.github.devrawr.scoreboards

import org.bukkit.entity.Player

open class ScoreboardEntry(
    open val player: Player,
    open var line: String,
    open val fixed: Boolean = false,
)
