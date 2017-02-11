package day1

fun main(args: Array<String>) {
    println(distanceOf("R2, R2, R2"))
    println(distanceOf("R5, L5, R5, R3"))


    println(distanceOfFirstRecurring("R8, R4, R4, R8"))
    val input = "R4, R1, L2, R1, L1, L1, R1, L5, R1, R5, L2, R3, L3, L4, R4, R4, R3, L5, L1, R5, R3, L4, R1, R5, L1, R3, L2, R3, R1, L4, L1, R1, L1, L5, R1, L2, R2, L3, L5, R1, R5, L1, R188, L3, R2, R52, R5, L3, R79, L1, R5, R186, R2, R1, L3, L5, L2, R2, R4, R5, R5, L5, L4, R5, R3, L4, R4, L4, L4, R5, L4, L3, L1, L4, R1, R2, L5, R3, L4, R3, L3, L5, R1, R1, L3, R2, R1, R2, R2, L4, R5, R1, R3, R2, L2, L2, L1, R2, L1, L3, R5, R1, R4, R5, R2, R2, R4, R4, R1, L3, R4, L2, R2, R1, R3, L5, R5, R2, R5, L1, R2, R4, L1, R5, L3, L3, R1, L4, R2, L2, R1, L1, R4, R3, L2, L3, R3, L2, R1, L4, R5, L1, R5, L2, L1, L5, L2, L5, L2, L4, L2, R3"
    println(distanceOf(input))
    println(distanceOfFirstRecurring(input))

}

fun follow(instructions: Sequence<Pair<Turn, Int>>, stepByStep: Boolean = false): Sequence<Pos>
{
    var pos = Pos(0, 0)
    var d = Direction.N

    return sequenceOf(pos) + instructions.map { (turn, go) ->
        d = d.turn(turn)
        val prevPos = pos
        pos = Pos(pos.x + d.dx * go, pos.y + d.dy * go)
        if (!stepByStep)
            sequenceOf(pos)
        else
            (1..go).asSequence().map { Pos(prevPos.x + d.dx * it, prevPos.y + d.dy * it) }
    }.flatten().constrainOnce()
}

fun String.toInstructions() = splitToSequence(",").map {
    it.trim().let { s -> Turn.valueOf(s.take(1)) to s.drop(1).toInt()}
}

fun distanceOf(input: String): Int {
    val instructions = input.toInstructions()
    val pos = follow(instructions).last()

    return pos.distance()
}

fun distanceOfFirstRecurring(input: String): Int {
    val instructions = input.toInstructions()
    val visited = mutableSetOf<Pos>()

    follow(instructions, stepByStep = true).forEach { pos ->
        if (!visited.add(pos)) return pos.distance()
    }
    error("No pos visited twice")
}

data class Pos(val x: Int, val y: Int)
fun Pos.distance() = Math.abs(x) + Math.abs(y)

enum class Direction(val dx: Int, val dy: Int) {
    N(0, 1),
    E(1, 0),
    S(0, -1),
    W(-1, 0);

    fun turn(turn: Turn): Direction =
        directions[(ordinal + (if (turn == Turn.L) -1 else 1) + directions.size) % directions.size]

    companion object {
        val directions = values().toList()
    }
}

enum class Turn {
    L,
    R
}
