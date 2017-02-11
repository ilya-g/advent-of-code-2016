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


val input = """
rect 1x1
rotate row r=0 by 20
rect 1x1
rotate row r=0 by 2
rect 1x1
rotate row r=0 by 3
rect 2x1
rotate row r=0 by 2
rect 1x1
rotate row r=0 by 3
rect 2x1
rotate row r=0 by 2
rect 1x1
rotate row r=0 by 4
rect 2x1
rotate row r=0 by 2
rect 1x1
rotate row r=0 by 2
rect 1x1
rotate row r=0 by 2
rect 1x1
rotate row r=0 by 3
rect 2x1
rotate row r=0 by 2
rect 1x1
rotate row r=0 by 5
rect 1x1
rotate row r=0 by 2
rect 1x1
rotate row r=0 by 6
rect 5x1
rotate row r=0 by 2
rect 1x3
rotate row r=2 by 8
rotate row r=0 by 8
rotate column c=0 by 1
rect 7x1
rotate row r=2 by 24
rotate row r=0 by 20
rotate column c=5 by 1
rotate column c=4 by 2
rotate column c=2 by 2
rotate column c=0 by 1
rect 7x1
rotate column c=34 by 2
rotate column c=22 by 1
rotate column c=15 by 1
rotate row r=2 by 18
rotate row r=0 by 12
rotate column c=8 by 2
rotate column c=7 by 1
rotate column c=5 by 2
rotate column c=2 by 1
rotate column c=0 by 1
rect 9x1
rotate row r=3 by 28
rotate row r=1 by 28
rotate row r=0 by 20
rotate column c=18 by 1
rotate column c=15 by 1
rotate column c=14 by 1
rotate column c=13 by 1
rotate column c=12 by 2
rotate column c=10 by 3
rotate column c=8 by 1
rotate column c=7 by 2
rotate column c=6 by 1
rotate column c=5 by 1
rotate column c=3 by 1
rotate column c=2 by 2
rotate column c=0 by 1
rect 19x1
rotate column c=34 by 2
rotate column c=24 by 1
rotate column c=23 by 1
rotate column c=14 by 1
rotate column c=9 by 2
rotate column c=4 by 2
rotate row r=3 by 5
rotate row r=2 by 3
rotate row r=1 by 7
rotate row r=0 by 5
rotate column c=0 by 2
rect 3x2
rotate column c=16 by 2
rotate row r=3 by 27
rotate row r=2 by 5
rotate row r=0 by 20
rotate column c=8 by 2
rotate column c=7 by 1
rotate column c=5 by 1
rotate column c=3 by 3
rotate column c=2 by 1
rotate column c=1 by 2
rotate column c=0 by 1
rect 9x1
rotate row r=4 by 42
rotate row r=3 by 40
rotate row r=1 by 30
rotate row r=0 by 40
rotate column c=37 by 2
rotate column c=36 by 3
rotate column c=35 by 1
rotate column c=33 by 1
rotate column c=32 by 1
rotate column c=31 by 3
rotate column c=30 by 1
rotate column c=28 by 1
rotate column c=27 by 1
rotate column c=25 by 1
rotate column c=23 by 3
rotate column c=22 by 1
rotate column c=21 by 1
rotate column c=20 by 1
rotate column c=18 by 1
rotate column c=17 by 1
rotate column c=16 by 3
rotate column c=15 by 1
rotate column c=13 by 1
rotate column c=12 by 1
rotate column c=11 by 2
rotate column c=10 by 1
rotate column c=8 by 1
rotate column c=7 by 2
rotate column c=5 by 1
rotate column c=3 by 3
rotate column c=2 by 1
rotate column c=1 by 1
rotate column c=0 by 1
rect 39x1
rotate column c=44 by 2
rotate column c=42 by 2
rotate column c=35 by 5
rotate column c=34 by 2
rotate column c=32 by 2
rotate column c=29 by 2
rotate column c=25 by 5
rotate column c=24 by 2
rotate column c=19 by 2
rotate column c=15 by 4
rotate column c=14 by 2
rotate column c=12 by 3
rotate column c=9 by 2
rotate column c=5 by 5
rotate column c=4 by 2
rotate row r=5 by 5
rotate row r=4 by 38
rotate row r=3 by 10
rotate row r=2 by 46
rotate row r=1 by 10
rotate column c=48 by 4
rotate column c=47 by 3
rotate column c=46 by 3
rotate column c=45 by 1
rotate column c=43 by 1
rotate column c=37 by 5
rotate column c=36 by 5
rotate column c=35 by 4
rotate column c=33 by 1
rotate column c=32 by 5
rotate column c=31 by 5
rotate column c=28 by 5
rotate column c=27 by 5
rotate column c=26 by 3
rotate column c=25 by 4
rotate column c=23 by 1
rotate column c=17 by 5
rotate column c=16 by 5
rotate column c=13 by 1
rotate column c=12 by 5
rotate column c=11 by 5
rotate column c=3 by 1
rotate column c=0 by 1
""".lines().filter { it.isNotEmpty() }

val input2 = File("src/day8/input2.txt").readLines().filter { it.isNotEmpty() }

