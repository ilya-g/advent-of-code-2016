package day8

import java.io.File

fun main(args: Array<String>) {
    val commands = input.map(patterns::parse).onEach(::println)

    val screen = Screen(6, 50)
    commands.forEach { screen.execute(it) }
    println(screen)

    println(screen.pixels.sumBy { it.count { it == '#' } })
    //test()
}

fun test() {
    val screen = Screen(3, 7)
    screen.executeAndPrint(TurnOn(2, 3))
    screen.executeAndPrint(RotateCol(1, 1))
    screen.executeAndPrint(RotateRow(0, 4))
    screen.executeAndPrint(RotateCol(1, 1))
}

fun Screen.executeAndPrint(command: Command) {
    println("command: $command")
    execute(command)
    println(this)
    println()
}
