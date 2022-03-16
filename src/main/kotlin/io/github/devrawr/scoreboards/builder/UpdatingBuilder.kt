package io.github.devrawr.scoreboards.builder

import io.github.devrawr.scoreboards.ScoreboardEntry

abstract class UpdatingBuilder<K : Any, V : Any>(
    internal val entry: ScoreboardEntry
)
{
    internal var handle: K? = null
    internal var hide: V? = null

    fun handle(action: K): UpdatingBuilder<K, V>
    {
        return this.apply {
            this.handle = action
        }
    }

    fun hide(action: V): UpdatingBuilder<K, V>
    {
        return this.apply {
            this.hide = action
        }
    }

    abstract fun create()
}