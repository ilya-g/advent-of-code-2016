package day22

import common.positionXY.*
import java.io.File

val input = File("src/day22/input.txt").readLines()

data class Node(val pos: Pos, val size: Int, val used: Int) {
    val avail = size - used
    override fun toString() = "$pos-size($size)-used($used)-avail($avail)"
}

enum class NodeState(val symbol: String) {
    STALE("#"),
    GOAL("G"),
    FULL("."),
    EMPTY("_"),
}


fun main(args: Array<String>) {
    val nodes = input.map(::parseNode)
    val nodesByAvail = nodes.sortedByDescending { it.avail }.asSequence()
    nodes.forEach(::println)


    val viablePairs = nodes.asSequence()
            .filter { it.used > 0 }
            .flatMap { a -> nodesByAvail.takeWhile { it.avail >= a.used }.filter { it !== a }.map { a to it } }


    println("viable pairs:")
    println(viablePairs.count())

    val movable = viablePairs.map { it.first }.toSet()
    val mx = nodes.map { it.pos.x }.max()!!
    val my = nodes.map { it.pos.y }.max()!!
    val goal = Pos(mx, 0)

    val nodeMap = nodes.associateTo(mutableMapOf()) {
        it.pos to when {
            it.used == 0 -> NodeState.EMPTY
            it in movable -> NodeState.FULL
            else -> NodeState.STALE
        }
    }
    check(nodeMap[goal] == NodeState.FULL)
    nodeMap[goal] = NodeState.GOAL

    for (y in 0..my) {
        println((0..mx).joinToString(" ") { x -> nodeMap[Pos(x, y)]!!.symbol })
    }

    val free = nodeMap.entries.single { it.value == NodeState.EMPTY }.key

    // ad-hoc solution for the given cluster layout
    val sx = mx + 1
    val steps1 = (sx - 1) + 5 * (sx - 2)
    println(steps1)
    val steps2 = free.x + free.y
    println(steps2)
    println(steps1 + steps2)
}


val regex = "/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T".toRegex()
fun parseNode(s: String): Node {
    val match = regex.find(s) ?: throw IllegalArgumentException(s)
    val (x, y, size, used, avail) = match.destructured
    return Node(Pos(x.toInt(), y.toInt()), size.toInt(), used.toInt()).also { check(it.avail == avail.toInt()) }
}
