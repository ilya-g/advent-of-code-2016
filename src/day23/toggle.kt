package day23

import day12.*

val input = """
cpy a b
dec b
cpy a d
cpy 0 a
cpy b c
inc a
dec c
jnz c -2
dec d
jnz d -5
dec b
cpy b c
cpy c d
dec d
inc c
jnz d -2
tgl c
cpy -16 c
jnz 1 c
cpy 79 c
jnz 74 d
inc a
inc d
jnz d -2
inc c
jnz c -5
""".lines().filter { it.isNotEmpty() }

fun main(args: Array<String>) {


    val program = input.map(::parse)
    println(program.joinToString("\n"))
//    val state = Reg.values().associate { it to 0 }
//    println(execute(program, state))
    val state2 = Reg.values().associate { it to 0 } + (Reg.a to 12)
    println(execute(program, state2))
}
