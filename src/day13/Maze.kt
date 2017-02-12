package day13

import day11.BfsSolver
import common.positionRC.*

class Maze(val seed: Int) {
    fun isWall(x: Int, y: Int) = Integer.bitCount(x*x + 3*x + 2*x*y + y + y*y + seed) % 2 == 1

    operator fun contains(pos: Pos) = (pos.c >= 0 && pos.r >= 0 && !isWall(pos.c, pos.r))

    fun lines(width: Int, height: Int) = (0 until height).map { r -> StringBuilder().apply {
        repeat(width) { c -> append(if (isWall(c, r)) "#" else ".") }
    }}
    fun toString(width: Int, height: Int) = lines(width, height).joinToString("\n")

}


val maze = Maze(1352)

fun main(args: Array<String>) {

    val initial = Pos(c = 1, r = 1)
    val target = Pos(c = 31, r = 39)
    val solver = BfsSolver(::nextMoves, { it == target }, { it })
    val moves = solver.solve(initial)!!
    println(moves.size)
    val lines = maze.lines(50, 45)
    var pos = initial
    for ((move, next) in moves) {
        lines[pos.r][pos.c] = move.name[0]
        pos = next
    }
    lines[pos.r][pos.c] = 'F'

    println(lines.joinToString("\n"))

}

fun nextMoves(pos: Pos): Sequence<Pair<Move, Pos>> {
    return Move.values.asSequence().mapNotNull { move ->
        val nextPos = pos.moveIn(move)
        if (nextPos in maze) move to nextPos else null
    }
}
