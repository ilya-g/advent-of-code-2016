package day19



val count = 3004953

fun main(args: Array<String>) {
    println(decimate1(count))
    //println(decimate2(count))
    println(decimate2a(count))

}

fun decimate1(count: Int): Int {
    val elves = (1..count).toMutableSet()
    var isTaking = true
    while (elves.size > 1) {
        elves.retainAll {
            return@retainAll isTaking.also { isTaking = !isTaking }
        }
    }
    return elves.single()
}

fun decimate2(count: Int): Int {
    val elves = MutableList(count) { it + 1 }
    while (elves.size > 1) {
        var index = 0
        while (index < elves.size) {
            val size = elves.size
            val distance = size / 2
            val pos = index + distance
            elves.removeAt(pos % size)
            if (pos < size) index++
        }
    }
    return elves.single()
}

fun decimate2a(count: Int): Int {
    val elves = MutableList<Int?>(count) { it + 1 }
    while (elves.size > 1) {
        var index = 0
        var removed = 0
        var skipped = 0
        while (index < elves.size) {
            if (elves[index] != null) {
                val remaining = elves.size - removed
                val distance = remaining / 2
                //elves.removeAt((index + distance) % remaining)
                val pos = (index + removed - skipped + distance) % elves.size
                elves[pos] = null
                removed++
            } else {
                skipped++
            }
            index++
        }
        elves.removeAll { it == null }
    }
    return elves.single()!!
}