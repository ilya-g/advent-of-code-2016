package day25

import common.assembunny.*


val input = """
cpy a d
cpy 9 c
cpy 282 b
inc d
dec b
jnz b -2
dec c
jnz c -5
cpy d a
jnz 0 0
cpy a b
cpy 0 a
cpy 2 c
jnz b 2
jnz 1 6
dec b
dec c
jnz c -4
inc a
jnz 1 -7
cpy 2 b
jnz c 2
jnz 1 4
dec b
dec c
jnz 1 -4
jnz 0 0
out b
jnz a -19
jnz 1 -21
""".lines().filter { it.isNotEmpty() }

sealed class ProgramTerminationException : Exception() {
    class Enough : ProgramTerminationException()
    class FailAt(val iteration: Int, val value: Int) : ProgramTerminationException()
}

fun main(args: Array<String>) {
    val program = input.map(::parse)
    println(program.joinToString("\n"))

    fun verifySequence(iteration: Int, value: Int) = value == (iteration % 2)

    fun evalAndCompare(program: List<OpCode>, key: Int, iterations: Int): Boolean {
        print("${key.toString().padStart(5)}: ")
        val state = Reg.values().associate { it to 0 } + (Reg.a to key)
        var iteration = 0
        try {
            execute(program, state) { value ->
                if (iteration >= iterations) throw ProgramTerminationException.Enough()
                if (!verifySequence(iteration, value)) throw ProgramTerminationException.FailAt(iteration, value)
                print(value)
                iteration++
            }
        }
        catch(e: ProgramTerminationException.FailAt) {
            println("${e.value} - failed at iteration ${e.iteration}")
            return false
        }
        catch (e: ProgramTerminationException.Enough) {
            println(" - success")
            return true
        }
        println(" - terminated preliminary")
        return false
    }


    for (key in 1..Int.MAX_VALUE) {
        if (evalAndCompare(program, key, 500)) break
    }
}

