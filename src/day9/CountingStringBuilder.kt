package day9

class CountingStringBuilder(length: Long) : Appendable, CharSequence {
    constructor() : this(0)
    override val length: Int get() = longLength.toInt()

    var longLength: Long = length
        private set

    override fun append(csq: CharSequence) = apply {
        longLength += csq.length
    }

    override fun append(csq: CharSequence, start: Int, end: Int) = apply {
        require(start in 0..csq.length)
        require(end in 0..csq.length)
        require(end >= start)
        longLength += end - start
    }

    override fun append(c: Char) = apply { longLength += 1 }

    override fun toString(): String = throw UnsupportedOperationException()
    override fun get(index: Int): Char = throw UnsupportedOperationException()

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        require(startIndex in 0..length)
        require(endIndex in 0..length)
        require(endIndex >= startIndex)
        return CountingStringBuilder((endIndex - startIndex).toLong())
    }
}