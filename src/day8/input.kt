package day8

import common.parsers.*
import java.io.File


val patterns = regexParsers<Command> {
    val int = groupParser(String::toInt)

    Regex("""rect (\d+)x(\d+)""") onMatch groups(int, int) { cols, rows -> TurnOn(rows, cols) }
    Regex("""rotate column c=(\d+) by (\d+)""") onMatch groups(int, int, ::RotateCol)
    Regex("""rotate row r=(\d+) by (\d+)""") onMatch groups(int, int, ::RotateRow)
}

//val rotateRegex = Regex("""rotate (column|row) (c|r)=(\d+) by (\d+)""")

//fun parseCommand(line: String): Command =
//        rectRegex.matchEntire(line)?.let {
//            val (c, r) = it.destructured
//            TurnOn(r.toInt(), c.toInt())
//        } ?:
//        rotateRegex.matchEntire(line)?.let {
//            val (type, coordType, coord, count) = it.destructured
//            when (type) {
//                "row" -> RotateRow(coord.toInt(), count.toInt())
//                "column" -> RotateCol(coord.toInt(), count.toInt())
//                else -> null
//            }
//        } ?:
//        error("Cannot parse '$line'")


val input = File("src/day8/input.txt").readLines().filter { it.isNotEmpty() }
val input2 = File("src/day8/input2.txt").readLines().filter { it.isNotEmpty() }

