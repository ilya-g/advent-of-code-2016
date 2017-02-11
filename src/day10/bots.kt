package day10

import common.parsers.*
import java.io.File

val input = File("src/day10/instructions.txt").readLines()


fun parseInput(lines: List<String>): Pair<List<Bot>, List<Output>> {
    val regexValue = Regex("value (\\d+) goes to bot (\\d+)")
    val regexTransfer = Regex("bot (\\d+) gives low to (\\w+ \\d+) and high to (\\w+ \\d+)")



    val bots = mutableMapOf<Int, Bot>()
    val outputs = mutableMapOf<Int, Output>()
    fun getBot(id: Int) = bots.getOrPut(id) { Bot(id) }
    fun getOutput(id: Int) = outputs.getOrPut(id) { Output(id) }

    val patterns = regexParsers<Unit> {
        val int = groupParser(String::toInt)
        val string = groupParser { it }

        regexValue onMatch groups(int, int) { chip, botId -> getBot(botId).takeChip(chip) }
        regexTransfer onMatch groups(int, string, string) { botId, out1, out2 ->
            fun parseOutput(out: String): ChipInput {
                val (type, id) = out.split(' ')
                return when (type) {
                    "bot" -> getBot(id.toInt())
                    "output" -> getOutput(id.toInt())
                    else -> error("Unknown type $type")
                }
            }
            val bot = getBot(botId)
            bot.lowGoesTo = parseOutput(out1)
            bot.highGoesTo = parseOutput(out2)
        }
    }

    lines.forEach(patterns::parse)

    return bots.values.sortedBy { it.id } to outputs.values.sortedBy { it.id }
}

fun main(args: Array<String>) {
    val (bots, outputs) = parseInput(input)
    do {
        val changes = bots.map { it.tryGiveChips() }.any { it }
    } while (changes)
    println(outputs.joinToString("\n"))
    println(outputs.take(3).fold(1) { acc, output -> acc * output.chip!! })
}

interface ChipInput {
    fun takeChip(chip: Int)
}
data class Output(val id: Int, var chip: Int? = null) : ChipInput {
    override fun takeChip(chip: Int) {
        check(this.chip == null) { "Output $id is already set"}
        this.chip = chip
    }
}
data class Bot(val id: Int, val chips: MutableList<Int> = mutableListOf()) : ChipInput {
    lateinit var highGoesTo: ChipInput
    lateinit var lowGoesTo: ChipInput

    override fun takeChip(chip: Int) {
        check(chips.size < 2) { "Bot $id can't take more chips: $chip" }
        chips.add(chip)
    }

    fun tryGiveChips(): Boolean {
        if (chips.size == 2) {
            val (low, high) = chips.sorted()
            if (low == 17 && high == 61) {
                println("Bot $id compares 17 with 61")
            }
            chips.clear()
            lowGoesTo.takeChip(low)
            highGoesTo.takeChip(high)
            return true
        }
        return false
    }
}