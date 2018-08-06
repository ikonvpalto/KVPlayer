package org.kvpbldsck.kvplayer.ui.commands

private val commands = mapOf(
        "open" to OpenCommand(),
        "close" to CloseCommand(),
        "play" to PlayCommand(),
        "pause" to PauseCommand(),
        "playPause" to PlayPauseCommand()
)

fun executeCommand(commandName: String, commandArgs: String): String {
    println("command $commandName, args: $commandArgs")

    return commands[commandName]?.execute(commandArgs)
            ?: "Command $commandName not found"
}