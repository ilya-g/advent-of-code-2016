package common.assembunny

enum class Reg {
    a,
    b,
    c,
    d
}

sealed class OpCode
// all days
data class Cpy(val from: Operand, val to: Operand) : OpCode()
data class Inc(val reg: Reg) : OpCode()
data class Dec(val reg: Reg) : OpCode()
data class Jnz(val operand: Operand, val offset: Operand) : OpCode()
// for day 23
data class Tgl(val offsetReg: Reg) : OpCode()
// for day 25
data class Out(val operand: Operand) : OpCode()

sealed class Operand
data class Ref(val reg: Reg): Operand()
data class Const(val value: Int): Operand()


fun parse(instruction: String): OpCode {
    val tokens = instruction.split(' ')
    return when (tokens[0]) {
        "cpy" -> Cpy(parseOperand(tokens[1]), parseOperand(tokens[2]))
        "inc" -> Inc(Reg.valueOf(tokens[1]))
        "dec" -> Dec(Reg.valueOf(tokens[1]))
        "jnz" -> Jnz(parseOperand(tokens[1]), parseOperand(tokens[2]))
        "tgl" -> Tgl(Reg.valueOf(tokens[1]))
        "out" -> Out(parseOperand(tokens[1]))
        else -> error("unknown opcode: $instruction")
    }
}

fun parseOperand(value: String): Operand =
        if (value.all { !it.isDigit() }) Ref(Reg.valueOf(value)) else Const(value.toInt())

fun toggle(opCode: OpCode): OpCode = when(opCode) {
    is Inc -> Dec(opCode.reg)
    is Dec -> Inc(opCode.reg)
    is Tgl -> Inc(opCode.offsetReg)
    is Jnz -> opCode.let { (op1, op2) -> Cpy(op1, op2) }
    is Cpy -> opCode.let { (op1, op2) -> Jnz(op1, op2) }
    else -> throw UnsupportedOperationException(opCode.toString())
}

fun execute(program: List<OpCode>, initialState: Map<Reg, Int>, handleOutputValue: (Int) -> Unit): Map<Reg, Int> {
    val program = program.toMutableList()
    var ip = 0
    val state = initialState.toMutableMap()
    fun eval(operand: Operand) = when(operand) {
        is Const -> operand.value
        is Ref -> state[operand.reg]!!
    }
    while (ip < program.size) {
        var jump = 1
        program[ip].run {
            when (this) {
                is Inc -> state[reg] = state[reg]!! + 1
                is Dec -> state[reg] = state[reg]!! - 1
                is Cpy ->
                    if (to is Ref) {
                        state[to.reg] = eval(from)
                    }
                is Jnz -> if (eval(operand) != 0) {
                    jump = eval(offset)
                }
                is Tgl -> {
                    val tglp = ip + state[offsetReg]!!
                    if (tglp in program.indices)
                        program[tglp] = toggle(program[tglp])
                }
                is Out ->
                    handleOutputValue(eval(operand))
            }
        }
        ip += jump
    }
    return state
}


fun execute(program: List<OpCode>, initialState: Map<Reg, Int>): Map<Reg, Int> = execute(program, initialState, { })


