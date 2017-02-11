package day6

import java.io.File

val input = File("src/day6/input.txt").readLines()

fun List<String>.mostFrequentAt(pos: Int): Char =
        groupingBy { it[pos] }.eachCount()
                .entries
                .maxBy { it.value }!!
                .key
fun List<String>.leastFrequentAt(pos: Int): Char =
        groupingBy { it[pos] }.eachCount()
                .entries
                .minBy { it.value }!!
                .key

fun main(args: Array<String>) {
    (0 until 8).map { input.mostFrequentAt(it) }.joinToString("").let(::println)
    (0 until 8).map { input.leastFrequentAt(it) }.joinToString("").let(::println)
}

