package day3

import java.io.File

val input: List<List<Int>> = File("src/day3/input.txt")
        .readLines()
        .mapTo(mutableListOf()) {
            it.split(" ")
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() }
        }


data class Triangle(val a: Int, val b: Int, val c: Int)

fun Triangle.isPossible() = (a + b) > c && (a + c) > b && (b + c) > a

fun main(args: Array<String>) {
    val triangles1 = input.map { (a, b, c) -> Triangle(a, b, c) }
    println(triangles1.count { it.isPossible() })

    val triangles2 = input.transposeBy3()
    println(triangles2.count { it.isPossible() })
}

fun List<List<Int>>.transposeBy3(): List<Triangle> {
    val result = mutableListOf<Triangle>()
    // TODO: Use chunked
    for (n in 0 until size step 3) {
        val (l1, l2, l3) = subList(n, n + 3)
        for (c in 0..2) {
            result.add(Triangle(l1[c], l2[c], l3[c]))
        }
    }
    return result
}