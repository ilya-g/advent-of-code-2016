package day13

import day11.BfsSolver

data class Pos(val c: Int, val r: Int)
enum class Move {
    L, R, U, D;
    companion object {
        val values = values().toList()
    }
}
fun Pos.next(move: Move) = when(move) {
    Move.L -> copy(c = c - 1)
    Move.R -> copy(c = c + 1)
    Move.U -> copy(r = r - 1)
    Move.D -> copy(r = r + 1)
}


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

    val initial = Pos(1, 1)
    val target = Pos(31, 39)
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
        val nextPos = pos.next(move)
        if (nextPos in maze) move to nextPos else null
    }
}
