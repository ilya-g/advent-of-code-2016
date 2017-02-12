package common.positionRC

data class Pos(val r: Int, val c: Int) {
    fun moveIn(move: Move): Pos = Pos(r + move.dr, c + move.dc)
}

enum class Move(val dr: Int, val dc: Int) {
    U(-1, 0), D(1, 0), R(0, 1), L(0, -1);
    companion object {
        val values = values().toList()
    }
}

