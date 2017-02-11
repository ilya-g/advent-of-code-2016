package day11

import day11.Elements.*
import kotlin.coroutines.experimental.buildSequence
import kotlin.system.measureNanoTime

enum class Elements {
    Pr,
    Pl,
    Co,
    Cu,
    Ru,
    El,
    Di;
    companion object {
        val size = values().size
    }
}

sealed class Item {
    abstract val element: Elements
}
data class RTG(override val element: Elements) : Item() {
    override fun toString() = "${element}G"
}
data class Chip(override val element: Elements) : Item() {
    override fun toString() = "${element}M"
}

class State(val floors: List<Set<Item>>, val elevatorAt: Int = 0) {
    init {
        require(elevatorAt < 4)
    }
    override fun toString(): String {
        fun pad(s: String) = s.padEnd(4, ' ')
        val allItems = floors.flatten().sortedBy { it.element }
        return floors.mapIndexed { floor, items -> buildString {
            append(pad("F${floor + 1}"))
            append(pad(if (elevatorAt == floor) "E" else "."))
            allItems.joinTo(this, "") { pad(if (it in items) it.toString() else ".") }
        }}.asReversed().joinToString("\n")
    }
    private val hashCode = floors.hashCode() xor elevatorAt
    override fun hashCode() = hashCode
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as State

        if (hashCode != other.hashCode) return false
        if (elevatorAt != other.elevatorAt) return false
        if (floors != other.floors) return false

        return true
    }

    fun compressInt(): Int {
        var sum = elevatorAt
        floors.forEachIndexed { fl, items ->
            items.forEach {
                sum += fl shl (2 + it.element.ordinal * 4 + if (it is RTG) 2 else 0)
            }
        }
        return sum
    }

    private val counts = IntArray(Elements.size)
    fun compressLong(): Long {
        var sum = elevatorAt
        var pairSum = 0
        floors.forEachIndexed { fl, items ->
            counts.fill(0)
            items.forEach { counts[it.element.ordinal]++ }
            val pairs = counts.count { it == 2 }
            pairSum += (pairs shl (fl * 3))
            items.forEach {
                if (counts[it.element.ordinal] < 2)
                    sum += fl * (4 shl (it.element.ordinal * 4 + if (it is RTG) 2 else 0))
            }
        }
        return (pairSum.toLong() shl 32) + sum
    }


}



fun Set<Item>.isChipsDamaged(): Boolean {
    if (none { it is RTG }) return false
    val rtgs = filterIsInstance<RTG>()
    return !all { it !is Chip || rtgs.any { rtg -> it.element == rtg.element } }
}


fun main(args: Array<String>) {
    val initialState = State(listOf(
            setOf(RTG(Pr), Chip(Pr), RTG(El), Chip(El), RTG(Di), Chip(Di)),
            setOf(RTG(Co), RTG(Cu), RTG(Pl), RTG(Ru)),
            setOf(Chip(Co), Chip(Cu), Chip(Pl), Chip(Ru)),
            setOf()
    ))
//    val initialState = State(listOf(
//            setOf(Chip(Co), Chip(Pl)),
//            setOf(RTG(Co)),
//            setOf(RTG(Pl)),
//            setOf()
//    ))
    println(initialState)
    println()

    val solver = BfsSolver(State::nextStates, State::isSolved, State::compressLong, sortedSetOf())
    var moves: List<Pair<String, State>>? = null
    val elapsed = measureNanoTime {
        moves = solver.solve(initialState)
    }


    println("${moves!!.size} moves, ${solver.visited.size} states visited, elapsed ${elapsed / 1000000} ms")
    moves!!.forEach { (move, state) ->
        println(move)
        println(state)
        println()
    }
}

fun State.isSolved() = (0..floors.lastIndex - 1).all { floors[it].isEmpty() }

fun State.nextStates(): Sequence<Pair<String, State>> {
    val firstNonEmpty = floors.indexOfFirst { it.isNotEmpty() }
    val nextFloors = listOf(elevatorAt - 1, elevatorAt + 1).filter { it in firstNonEmpty..floors.lastIndex }
    val items = floors[elevatorAt]
    check(items.isNotEmpty())
    val itemsToMove = items.itemsToMove()
    return buildSequence {
        for (set in itemsToMove) {
            val left = items - set
            if (left.isChipsDamaged()) continue
            for (nextFloor in nextFloors) {
                val nextFloorItems = floors[nextFloor] + set
                if (nextFloorItems.isChipsDamaged()) continue

                val nextState = State(floors.mapIndexed { fl, items ->
                    when(fl) {
                        elevatorAt -> left
                        nextFloor -> nextFloorItems
                        else -> items
                    }
                }, elevatorAt = nextFloor)
                yield("$set to ${nextFloor+1} floor" to nextState)
            }
        }
    }
}

fun Set<Item>.itemsToMove(): List<Set<Item>> = run {
    val list = toList()
    val result = mutableListOf<Set<Item>>()
    for (i in 0..list.lastIndex) {
        for (j in i + 1..list.lastIndex) {
            result.add(setOf(list[i], list[j]))
        }
    }
    list.mapTo(result, { setOf(it) })
    return result
}

//fun Set<Item>.itemsToMove(): Sequence<Set<Item>> = buildSequence {
//    val list = toList()
//    list.forEach { yield(setOf(it)) }
//    for (i in 0..list.lastIndex) {
//        for (j in i + 1..list.lastIndex) {
//            yield(setOf(list[i], list[j]))
//        }
//    }
//}

