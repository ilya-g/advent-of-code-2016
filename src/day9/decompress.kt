package day9
import java.io.File
import kotlin.reflect.KProperty

val input = File("src/day9/input2.txt").readText()

//class X {
//    val p1: Int = 0
//    val p2: Int? = null
//    val p3: java.lang.Integer? = null
//}

fun main(args: Array<String>) {

    test("ADVENT")
    test("A(1x5)BC")
    test("(3x3)XYZ")
    test("A(2x2)BCD(2x2)EFG")
    test("(6x1)(1x3)A")
    test("X(8x2)(3x3)ABCY")
    test("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN")

    println(input.any { it.isWhitespace() })
    println(input.decompress(recursive = false).length)
    println(input.decompress(::CountingStringBuilder).longLength)
}

fun test(s: String) {
    val ds = s.decompress()
    println("'$s' decompressed to '$ds' with length=${ds.length}")
}

val regexMarker = Regex("""\((\d+)c(\d+)\)""")


fun String.decompress(recursive: Boolean = true): String =
        decompress(::StringBuilder, recursive).toString()

fun <TBuilder> String.decompress(builder: () -> TBuilder, recursive: Boolean = true): TBuilder
        where TBuilder : Appendable, TBuilder : CharSequence {
    let { s ->
        with(builder()) {
            var startPos = 0
            while (true) {
                val marker = regexMarker.find(s, startPos)
                if (marker == null) {
                    append(s.substring(startPos))
                    break
                }

                append(s.substring(startPos, marker.range.start))
                val (len, rep) = marker.destructured
                val blockStart = marker.range.endInclusive + 1
                val blockEnd = (blockStart + len.toInt()).coerceAtMost(s.length)
                val block = s.substring(blockStart, blockEnd).let {
                    if (recursive) it.decompress(builder, recursive) else it
                }
                repeat(rep.toInt()) {
                    append(block)
                }
                startPos = blockEnd
            }
            return this
        }
    }
}

