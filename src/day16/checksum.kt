package day16

val input = "10111100110001111"

fun main(args: Array<String>) {
    val required = 35651584 * 32 / 17 * 15
    val builder = StringBuilder(required)
    builder.append(input)
    while (builder.length < required) {
        enlarge(builder, required)
    }
    println(checksum(builder))
}

fun enlarge(input: StringBuilder, required: Int) {
    val size = input.length
    input.append('0')
    for (i in size - 1 downTo 0) {
        input.append(if (input[i] == '0') '1' else '0')
        if (input.length == required) break
    }

}

fun checksum(input: StringBuilder): CharSequence = if (input.length % 2 == 1) input else run {
        val newLength = input.length / 2
        for (i in 0..newLength - 1) {
            input[i] = if (input[i*2] == input[i*2 + 1]) '1' else '0'
        }
        input.setLength(newLength)
        checksum(input)
    }
