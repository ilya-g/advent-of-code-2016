package day4

import java.io.File

val input = File("src/day4/input.txt").readLines()

data class RoomId(val components: List<String>, val sectorId: Int, val checksum: String)
fun roomFromString(room: String): RoomId {
    val components = room.split('-')
    val lastPart = components.last()
    val sectorId = lastPart.substringBefore('[').toInt()
    val checksum = lastPart.substringAfter('[').removeSuffix("]")
    return RoomId(components.dropLast(1), sectorId, checksum)
}

fun RoomId.isValid(): Boolean {
    val actualChecksum =
            components
                    .flatMap { it.asIterable() }
                    .groupBy { it }
                    .map { it.key to it.value.size }
                    .sortedWith(compareByDescending<Pair<Char, Int>> { it.second }.thenBy { it.first })
                    .take(5)
                    .joinToString("") { it.first.toString() }

    return actualChecksum == this.checksum
}

fun RoomId.decrypt(): RoomId =
        copy(components.map { c -> c.map { it.rotate(sectorId) }.joinToString("") })

fun Char.rotate(n: Int): Char = 'a' + (this - 'a' + n) % 26

fun main(args: Array<String>) {
    val rooms = input.map(::roomFromString)
    val validRooms = rooms.mapNotNull { if (it.isValid()) it.decrypt() else null }
    println(validRooms.sumBy { it.sectorId })
    println(validRooms.find { it.components.any { it.contains("North", ignoreCase = true) }})
}