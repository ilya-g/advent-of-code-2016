package day21


import common.parsers.*
import java.io.File
import java.util.*


val input = File("src/day21/input.txt").readLines()

fun main(args: Array<String>) {
    val operations = input.map(patterns::parse)
    operations.forEach(::println)

    val original = "abcdefgh".toList()
    val result = operations.fold(original) { acc, operation -> operation.action(acc) }
    println(result.joinToString(""))

    val result2 = "fbgdceah".toList()
    val original2 = operations.foldRight(result2) { operation, acc -> operation.reverse.action(acc) }
    println(original2.joinToString(""))
}


val patterns = regexParsers<Operation> {
    val string: GroupParser<String> = {it}
    val int: GroupParser<Int> = String::toInt
    val char: GroupParser<Char> = String::single

    Regex("swap letter ([a-z]) with letter ([a-z])")    onMatch groups(char, char, ::Swap)
    Regex("move position (\\d) to position (\\d)")      onMatch groups(int, int, ::Move)
    Regex("swap position (\\d) with position (\\d)")    onMatch groups(int, int, ::SwapPos)
    Regex("reverse positions (\\d) through (\\d)")      onMatch groups(int, int) { a, b -> Reverse(a..b) }
    Regex("rotate (left|right) (\\d) steps?")           onMatch groups(string, int) { dir, steps -> Rotate(steps * if (dir == "left") -1 else 1) }
    Regex("rotate based on position of letter ([a-z])") onMatch groups(char, ::RotateOn)
}

typealias Buffer = List<Char>
abstract class Operation(val action: (Buffer) -> Buffer) {
    open val reverse: Operation get() = this
}

data class Swap(val letter1: Char, val letter2: Char) : Operation({
            it.map { c ->
                when (c) {
                    letter1 -> letter2
                    letter2 -> letter1
                    else -> c
                }
            }
        })
data class SwapPos(val from: Int, val to: Int) : Operation({
            it.toMutableList().apply {
                val tmp = this[from]
                this[from] = this[to]
                this[to] = tmp
            }
})
data class Move(val from: Int, val to: Int) : Operation({
    it.toMutableList().apply {
        val l = removeAt(from)
        add(to, l)
    }
}) {
    override val reverse: Operation by lazy { Move(to, from) }
}

data class Reverse(val range: IntRange): Operation({
    it.toMutableList().apply { subList(range.start, range.endInclusive + 1).reverse() }
})

data class Rotate(val steps: Int) : Operation({
    it.toMutableList().also {
        Collections.rotate(it, steps)
    }
}) {
    override val reverse: Operation by lazy { Rotate(-steps) }
}

data class RotateOn(val letter: Char) : Operation({ rotate(it, letter, Companion::forwardSteps) }) {
    override val reverse: Operation = object : Operation({ rotate(it, letter, Companion::backwardSteps) }) {}

    companion object {
        fun forwardSteps(index: Int) = 1 + index + if(index >= 4) 1 else 0
        val backwardRotationMap = (0 until 8).associate { i1 ->
            val i2 = (i1 + forwardSteps(i1)) % 8
            i2 to (i1 - i2)
        }
        fun backwardSteps(index: Int) = backwardRotationMap[index]!!
        fun rotate(buffer: List<Char>, letter: Char, stepsForIndex: (Int) -> Int): List<Char> {
            val index = buffer.indexOf(letter).also { check(it >= 0) }
            return buffer.toMutableList().also {
                Collections.rotate(it, stepsForIndex(index) )
            }
        }
    }
}




