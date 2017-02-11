package day5

import common.md5.*

val input = "reyedfim"


fun hashes(seed: String): Sequence<String> =
        generateSequence(1, { it + 1 }).map {
            md5hex(seed + it)
        }

fun password(room: String) = run {
    println("     V")
    hashes(room)
            .filter { it.startsWith("00000") }
            .onEach(::println)
            .take(8)
            .joinToString("") { it[5].toString() }

}

fun password2(room: String) = buildString {
    setLength(8)
    println("     PV")
    hashes(room)
            .filter { it.startsWith("00000") }
            .forEach {
                val pos = it[5].toString().toInt(radix = 16)
                if (pos < 8 && this[pos] == 0.toChar()) {
                    println(it)
                    this[pos] = it[6]
                }
                if (this.all { it != 0.toChar()}) return@buildString
            }
}

fun main(args: Array<String>) {
    password(input).let(::println)
    password2(input).let(::println)
}


