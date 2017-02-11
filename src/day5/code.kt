package day5

val input = "reyedfim"

val md5 = java.security.MessageDigest.getInstance("MD5")
fun md5(s: String) = md5.digest(s.toByteArray())
fun ByteArray.toHexString() = joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }

fun hashes(seed: String): Sequence<String> =
        generateSequence(1, { it + 1 }).map {
            md5(seed + it).toHexString()
        }

fun password(room: String) = run {
    println("     V")
    hashes(room)
            .filter { it.startsWith("00000") }
            .onEach(::println)
            .take(8)
            .joinToString("") { it[5].toString() }

}

fun password2(room: String) = buildString {
    setLength(8)
    println("     PV")
    hashes(room)
            .filter { it.startsWith("00000") }
            .forEach {
                val pos = it[5].toString().toInt(radix = 16)
                if (pos < 8 && this[pos] == 0.toChar()) {
                    println(it)
                    this[pos] = it[6]
                }
                if (this.all { it != 0.toChar()}) return@buildString
            }
}

fun main(args: Array<String>) {
    password(input).let(::println)
    password2(input).let(::println)
}


