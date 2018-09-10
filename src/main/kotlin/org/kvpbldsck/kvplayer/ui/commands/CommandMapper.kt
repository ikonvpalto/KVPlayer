package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.extensions.normalize

private val commands = mapOf(
        add(OpenCommand()),
        add(CloseCommand()),
        add(PlayCommand()),
        add(PauseCommand()),
        add(PlayPauseCommand()),
        add(SetVolumeCommand()),
        add(HelpCommand())
)

private fun add(command: Command): Pair<String, Command> = command.name.normalize() to command

fun executeCommand(commandName: String, commandArgs: String): String {
    println("command $commandName, args: $commandArgs")

    return commands[commandName]?.execute(commandArgs)
            ?: "Command $commandName not found"
}

val commandSet = commands.values