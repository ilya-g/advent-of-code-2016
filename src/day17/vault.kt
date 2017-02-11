package day17

import day11.BfsSolver
import day13.Move
import day13.Move.*
import day13.Pos
import day13.next
import common.md5.*
import kotlin.coroutines.experimental.buildSequence

data class State(val seed: String, val pos: Pos, val path: List<Move>)

val moves = listOf(U, D, L, R)
fun State.openDoors(): Sequence<Pair<Move, State>> {
    val str = buildString(seed.length + path.size) {
        append(seed)
        path.forEach { append(it.name) }
    }
    val md5 = md5hex(str)
    return buildSequence {
        moves.forEachIndexed { index, move ->
            if (md5[index] in 'b'..'f') {
                val nextPos = pos.next(move)
                if (nextPos.r in 0..3 && nextPos.c in 0..3)
                    yield(move to copy(pos = nextPos, path = path + move))
            }
        }
    }
}

class CountingSet<T> : AbstractMutableSet<T>() {
    override var size: Int = 0
        private set
    override fun iterator(): MutableIterator<T> = object : MutableIterator<T>, Iterator<T> by emptyList<T>().iterator() {
        override fun remove() = throw IllegalStateException()
    }

    override fun contains(element: T): Boolean = false
    override fun add(element: T): Boolean = true.also { size += 1 }
    override fun remove(element: T): Boolean = false
}

fun main(args: Array<String>) {
    val initial = Pos(0, 0)
    val target = Pos(3, 3)
    val solver = BfsSolver(State::openDoors, { it.pos == target }, { it }, CountingSet())
    val solutions = solver.solveAll(State("hhhxzeay", initial, emptyList()))
            .onEach { println("Path: ${it.joinToString("") { (move) -> move.name }}") }
            .toList()

    println(solutions.first().joinToString("") { (move) -> move.name })
    println(solutions.map { it.size }.max())
}
