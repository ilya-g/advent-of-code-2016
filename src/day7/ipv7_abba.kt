package day7

import java.io.File

val input = File("src/day7/input.txt").readLines().map { Address.parse(it) }



data class Address(val normal: List<String>, val squared: List<String>) {
    companion object {
        private val sq_regex = Regex("""\[[a-z]+\]""")
        fun parse(address: String): Address {
            val squareSegments = sq_regex.findAll(address).map { it.value.removeSurrounding("[", "]") }.toList()
            val segments = address.split(sq_regex)
            return Address(segments, squareSegments)
        }

    }
}

fun String.hasABBA(): Boolean {
    fun String.isAbbaAt(pos: Int) =
            this[pos] != this[pos + 1] &&
                    this[pos] == this[pos + 3] &&
                    this[pos + 1] == this[pos + 2]

    return (0..(length - 4)).any { isAbbaAt(it) }
}

fun Address.findABAs(): Sequence<String> {
    fun String.abaAt(pos: Int) =
        if (this[pos] != this[pos + 1] && this[pos] == this[pos + 2]) this.slice(pos..pos+2) else null

    return normal.asSequence().map { seg -> (0..seg.length - 3).mapNotNull { seg.abaAt(it) } }.flatten()
}

fun Address.supportTLS() = squared.none { it.hasABBA() } && normal.any { it.hasABBA() }

fun Address.supportSSL():Boolean = findABAs().any { aba ->
    val bab = aba.slice(listOf(1, 0, 1))
    squared.any { bab in it }
}


fun test(input: String) {
    println("$input, supports TLS: ${Address.parse(input).supportTLS()}")
}

fun main(args: Array<String>) {
    test("abba[mnop]qrst")
    test("abcd[bddb]xyyx")
    test("aaaa[qwer]tyui")
    test("ioxxoj[asdfgh]zxcvbn")
    println(input.count { it.supportTLS() })

    println(input.count { it.supportSSL() })
    //println(Address.parse("zazbz[bzb]cdb").supportSSL())

}