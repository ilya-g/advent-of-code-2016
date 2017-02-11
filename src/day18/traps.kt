package day18


fun trapsAfter(traps: String) = buildString {
    for (index in traps.indices) {
        val (l, c, r) = (-1..1).map { traps.getOrElse(index - it) {'.'} }
        append(trapAfter(l, c, r))
    }
}

fun trapAfter(l: Char, c: Char, r: Char): Char =
        if (l != r && (l == c || r == c)) '^' else '.'

fun main(args: Array<String>) {
    val input = "^.^^^..^^...^.^..^^^^^.....^...^^^..^^^^.^^.^^^^^^^^.^^.^^^^...^^...^^^^.^.^..^^..^..^.^^.^.^......."
    val rows = generateSequence(input, ::trapsAfter).take(400000)
    // rows.toList().forEach(::println)

    println(rows.sumBy { it.count { c -> c == '.' } })
}