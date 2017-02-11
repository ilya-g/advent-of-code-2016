package day24


import day11.BfsSolver
import day2.Pos
import java.io.File
import kotlin.coroutines.experimental.buildSequence
import kotlin.system.measureNanoTime

val input = File("src/day24/map.txt").readLines()

fun parseMap(lines: List<String>) =
        DuctMap(lines.size, lines.map { it.length }.max()!!) { r, c ->
            lines[r].getOrElse(c, { '#' }).let {
                when {
                    it == '#' -> Cell.Wall
                    it.isDigit() -> Cell.Point(it.toString().toInt(), Pos(r, c))
                    it == '.' -> Cell.Empty
                    else -> error("Unknown cell type '$it'")
                }
            }
        }


typealias Stop = Int
typealias Path = List<Stop>

sealed class Cell {
    object Wall : Cell()
    object Empty : Cell()
    data class Point(val id: Stop, val pos: Pos) : Cell()
}


class DuctMap(val rows: Int, val cols: Int, init: (r: Int, c: Int) -> Cell) {
    val cells: List<Cell>
    val locations: Map<Stop, Pos>

    init {
        cells = List(rows * cols) { i -> init(i / cols, i % cols) }
        locations = cells.mapNotNull { (it as? Cell.Point) }.associateBy({ it.id }, { it.pos })
    }

    operator fun get(r: Int, c: Int) = cells[r * cols + c]
    operator fun get(p: Pos) = get(p.r, p.c)
}



val deltas = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
fun DuctMap.findDistances(): Map<Pair<Stop, Stop>, Int> {
    fun nextPos(pos: Pos) = buildSequence {
        val (r, c) = pos
        for (move in deltas) {
            val (dr, dc) = move
            val r1 = r + dr
            val c1 = c + dc
            if (r1 in 0..rows - 1 && c1 in 0..cols - 1 && get(r1, c1) != Cell.Wall) yield(move to Pos(r1, c1))
        }
    }

    val results = mutableMapOf<Pair<Stop, Stop>, Int>()
    fun distancesFrom(start: Stop): Sequence<Pair<Pair<Stop, Stop>, Int>> {
        val startPos = locations[start]!!
        val solver = BfsSolver(::nextPos, { it != startPos && get(it) is Cell.Point }, { it }, walkOverSolution = true).apply { quiet = true }
        return solver.solveAll(startPos).map { s ->
            val (_, endPos) = s.last()
            val endCell = get(endPos) as Cell.Point
            (start to endCell.id) to s.size
        }
    }

    measureNanoTime {
        //locations.keys.parallelStream().map { distancesFrom(it).toList() }.toList().forEach { it.toMap(results) }
        locations.keys.forEach { distancesFrom(it).toMap(results) }
    }.let(::println)

    return results
}



fun main(args: Array<String>) {
    val map = parseMap(input)
    println(map.locations)
    val distances = map.findDistances()
    distances.entries.forEach(::println)
    distances.forEach { (k, d) -> check(d == distances[k.second to k.first]) { ("$k, $d") } }

    fun allPaths(prefix: Path, points: List<Stop>): Sequence<Path> =
        if (points.isEmpty())
            sequenceOf(prefix)
        else
            points.asSequence().flatMap { p -> allPaths(prefix + p, points - p) }

    fun Path.length() = zip(drop(1)) { p1, p2 -> distances[p1 to p2]!! }.sum()

    val locations = map.locations.keys.toList()
    val allPathsStartingAt0 = allPaths(listOf(0), locations - 0)

    val shortestPath = allPathsStartingAt0.minBy(Path::length)!!
    val shortestPathAndReturn = allPathsStartingAt0.map { it + 0 }.minBy(Path::length)!!

    println(shortestPath.length())
    println(shortestPathAndReturn.length())
}