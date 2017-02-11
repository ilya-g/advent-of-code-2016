package day8

class Screen(val rows: Int, val cols: Int) {
    val pixels = List(rows) { MutableList(cols) { '.' }}

    fun execute(command: Command): Unit = when (command) {
        is TurnOn -> {
            repeat(command.rows) { r ->
                repeat(command.cols) { c ->
                    pixels[r][c] = '#'
                }
            }
        }
        is RotateRow -> {
            pixels[command.row].rotate(command.count)
        }
        is RotateCol -> {
            val col = MutableList(rows) { row -> pixels[row][command.col] }
            col.rotate(command.count)
            repeat(rows) { row -> pixels[row][command.col] = col[row] }
        }
    }

    override fun toString(): String {
        return pixels.joinToString("\n") { it.joinToString("") }
    }
}

sealed class Command
data class TurnOn(val rows: Int, val cols: Int) : Command()
data class RotateRow(val row: Int, val count: Int) : Command()
data class RotateCol(val col: Int, val count: Int): Command()


// to stdlib

fun MutableList<*>.rotate(distance: Int) = java.util.Collections.rotate(this, distance)
