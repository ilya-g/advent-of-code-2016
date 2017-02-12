package day18


fun trapsAfter(traps: String) = buildString {
    for (index in traps.indices) {
        val (l, c, r) = (-1..1).map { traps.getOrElse(index - it) {'.'} }
        append(trapAfter(l, c, r))
    }
}

fun trapAfter(l: Char, c: Char, r: Char): Char =
        if (l != r && (l == c || r == c)) '^' else '.'

fun Sequence<String>.safeTileCount(): Int = sumBy { it.count { c -> c == '.' } }

fun main(args: Array<String>) {
    val input = "^.^^^..^^...^.^..^^^^^.....^...^^^..^^^^.^^.^^^^^^^^.^^.^^^^...^^...^^^^.^.^..^^..^..^.^^.^.^......."

    val rows = generateSequence(input, ::trapsAfter)
    println(rows.take(40).onEach(::println).safeTileCount())

    println(rows.take(400000).safeTileCount())
}