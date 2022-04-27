package io.github.devrawr.scoreboards.order

enum class LineOrder
{
    Decrement
    {
        override fun getRelativePosition(index: Int) = 16 - index
    },
    Increment
    {
        override fun getRelativePosition(index: Int) = index
    };

    abstract fun getRelativePosition(index: Int): Int
}