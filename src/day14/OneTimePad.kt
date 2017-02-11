package day14

import common.md5.*
import java.util.*
import kotlin.coroutines.experimental.buildSequence


val regex3 = Regex("(.)\\1{2}")
val regex5 = Regex("(.)\\1{4}")
fun hashes(seed: String) = (0..Int.MAX_VALUE).asSequence().map { md5hex(seed + it) }

fun Iterator<*>.iterate(n: Int) = repeat(n) { if (hasNext()) next() else return }
inline fun <T> Queue<T>.pollIf(predicate: (T) -> Boolean): T? = peek()?.let { if (predicate(it)) poll() else null }
tailrec fun md5stretch(input: String, count: Int): String {
    val hash = md5hex(input)
    return if (count > 1) md5stretch(hash, count - 1) else hash
}

fun keyIndices(seed: String) = buildSequence<Int> {
    val match3At = ArrayDeque<Pair<Int, Char>>()
    val match5At = ArrayDeque<Pair<Int, List<Char>>>()
    var generateIndex = 0
    fun generate() {
        val hash = md5stretch(seed + generateIndex, 2017)
        val tripleMatch = regex3.find(hash)
        if (tripleMatch != null) {
            match3At.offer(generateIndex to tripleMatch.groupValues[0][1])
            val allFives = regex5.findAll(hash).map { it.groupValues[1][0] }.toList()
            if (allFives.isNotEmpty())
                match5At.offer(generateIndex to allFives)
        }
        generateIndex++
    }

    for (keyIndex in 0..Int.MAX_VALUE) {
        while (generateIndex <= keyIndex + 1000) generate()
        match5At.pollIf { it.first == keyIndex }
        match3At.pollIf { it.first == keyIndex }
                ?.let { (_, char) ->
                    if (match5At.any { char in it.second }) {
                        yield(keyIndex)
                    }
                }
    }
}

fun main(args: Array<String>) {
    val indices = keyIndices("qzyelonm")
    indices.take(64).forEach {
        println(it)
    }
}