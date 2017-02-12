package day12

import common.assembunny.*

val input = """
cpy 1 a
cpy 1 b
cpy 26 d
jnz c 2
jnz 1 5
cpy 7 c
inc d
dec c
jnz c -2
cpy a c
inc a
dec b
jnz b -2
cpy c b
dec d
jnz d -6
cpy 14 c
cpy 14 d
inc a
dec d
jnz d -2
dec c
jnz c -5
""".lines().filter { it.isNotEmpty() }


fun main(args: Array<String>) {
    val program = input.map(::parse)
    println(program.joinToString("\n"))
    val state = Reg.values().associate { it to 0 }
    println(execute(program, state))
    val state2 = state + (Reg.c to 1)
    println(execute(program, state2))
}

