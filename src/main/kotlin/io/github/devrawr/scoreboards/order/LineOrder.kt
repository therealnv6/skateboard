package io.github.devrawr.scoreboards.order

enum class LineOrder
{
    Decrement,
    Increment;

    fun getRelativePosition(index: Int): Int
    {
        return when (this)
        {
            Decrement -> 16 - index
            Increment -> index
        }
    }
}