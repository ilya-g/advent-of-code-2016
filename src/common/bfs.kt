package common.bfs

import common.bfs.LList.*
import java.util.*
import kotlin.coroutines.experimental.buildSequence

fun <T, M> BfsSolver(next: (T) -> Sequence<Pair<M, T>>, isSolved: (T) -> Boolean, visited: MutableSet<T> = mutableSetOf(), walkOverSolution: Boolean = false) =
        BfsSolver(next, isSolved, { it }, visited, walkOverSolution)

class BfsSolver<T, M, S>(val next: (T) -> Sequence<Pair<M, T>>, val isSolved: (T) -> Boolean, val compress: (T) -> S, val visited: MutableSet<S> = mutableSetOf(), val walkOverSolution: Boolean = false) {
    private data class StateWithPath<out M, out T>(val state: T, val path: LList<Pair<M, T>>)

    var quiet = false

    private var pathSize: Int = 0
    private val queue: Queue<StateWithPath<M, T>> = ArrayDeque()

    fun solve(current: T): List<Pair<M, T>>? = solveAll(current).firstOrNull()
    fun solveAll(current: T): Sequence<List<Pair<M, T>>> = buildSequence {
        if (isSolved(current)) yield(emptyList())

        queue.add(StateWithPath(current, Nil))
        visited.add(compress(current))

        while (queue.isNotEmpty()) {
            val (state, path) = queue.poll()!!

            if (path.size > pathSize || pathSize == 0) {
                pathSize = path.size
                if (!quiet)
                    println("Analyzing paths of size $pathSize, queue size ${queue.size}, visited states ${visited.size}")
            }

            for (next in next(state)) {
                val (_, nextState) = next
                val nextStateC = compress(nextState)
                if (nextStateC !in visited) {
                    val nextPath = Cons(next, path)
                    if (isSolved(nextState)) {
                        yield(nextPath.reversed())
                        if (!walkOverSolution) continue
                    }
                    visited.add(nextStateC)
                    queue.add(StateWithPath(nextState, nextPath))
                }
            }
        }
    }
}

sealed class LList<out T> : Iterable<T> {
    abstract val size: Int
    data class Cons<out T>(val head: T, val tail: LList<T>) : LList<T>() {
        override val size: Int = tail.size + 1
        override fun iterator(): Iterator<T> = generateSequence(this) { it.tail as? Cons }.map { it.head }.iterator()
//        override fun iterator1(): Iterator<T> = buildSequence {
//            yield(head)
//            var tail = tail
//            while (tail is Cons) {
//                yield(tail.head)
//                tail = tail.tail
//            }
//        }.iterator()
    }
    object Nil : LList<Nothing>() {
        override val size: Int get() = 0
        override fun iterator(): Iterator<Nothing> = emptyList<Nothing>().iterator()
    }
}
