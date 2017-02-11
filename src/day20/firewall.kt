package day20

import java.io.File

fun main(args: Array<String>) {
    val ranges = File("src/day20/input.txt").readLines()
            .map { it.split('-').let { (s, e) -> s.toLong()..e.toLong() }}
            .sortedBy { it.start }

    val joined = unionRanges(ranges)

    println("${ranges.size}, ${joined.size}")
    joined.forEach(::println)

    println(findFirstFree(ranges))
    println(findFirstFree(joined))
    println(joined.first().endInclusive + 1)

    val count = (1L shl 32) - joined.map { it.endInclusive - it.start + 1 }.sum()
    println(count)
}

fun findFirstFree(ranges: List<LongRange>): Long {
    if (ranges.isEmpty() || ranges[0].start > 0) return 0
    for (i in ranges.indices) {
        val nextValue = (ranges[i].endInclusive + 1)
        if (ranges.none { nextValue in it }) {
            return nextValue
        }
    }
    error("No free address is found")
}

fun unionRanges(ranges: List<LongRange>): List<LongRange> {
    val result = mutableListOf<LongRange>()
    var i = 0
    while (i < ranges.size) {
        var r = ranges[i]
        i++
        while (i < ranges.size && ranges[i].let { r1 -> r overlaps r1 || r conjoins r1}) {
            r = r.start..maxOf(r.endInclusive, ranges[i].endInclusive)
            i++
        }
        result += r
    }
    return result
}

infix fun LongRange.overlaps(other: LongRange) = this.start <= other.endInclusive && other.start <= this.endInclusive
infix fun LongRange.conjoins(other: LongRange) = other.start == this.endInclusive + 1


