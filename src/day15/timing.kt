package day15

val input = """
Disc #1 has 17 positions; at time=0, it is at position 1.
Disc #2 has 7 positions; at time=0, it is at position 0.
Disc #3 has 19 positions; at time=0, it is at position 2.
Disc #4 has 5 positions; at time=0, it is at position 0.
Disc #5 has 3 positions; at time=0, it is at position 0.
Disc #6 has 13 positions; at time=0, it is at position 5.
""".lines().filter { it.isNotEmpty() }

data class Disc(val pos: Int, val count: Int)

infix fun Int.mod(that: Int) = (this.rem(that) + that).rem(that)

fun inverseMod(a: Int, b: Int): Int {
    var p = a mod b
    var r = 1
    while (p != 1) {
        p = (p + a) mod b
        r += 1
    }
    return r
}

fun main(args: Array<String>) {
    val discs = parseDiscs(input)
    println(findRightTiming(discs))
    println(findRightTiming(discs + Disc(0, 11)))

}

private fun findRightTiming(discs: List<Disc>): Int {
    val M = discs.fold(1) { acc, disc -> acc * disc.count }
    val t = discs.mapIndexed { index, (pos, count) ->
        val r = (count - (index + 1 + pos)) mod count
        val Mi = M / count
        val Minv = inverseMod(Mi, count)
        r * Mi * Minv
    }.sum() mod M
    return t
}

val regex = "Disc #\\d+ has (\\d+) positions; at time=0, it is at position (\\d+).".toRegex()
fun parseDiscs(input: List<String>): List<Disc> = input.map {
    val (count, pos) = regex.matchEntire(it)!!.destructured
    Disc(pos.toInt(), count.toInt())
}


