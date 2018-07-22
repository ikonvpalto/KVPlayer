package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.extensions.normalize

private val commands = mapOf(
        "open" to OpenCommand(),
        "close" to CloseCommand(),
        "play" to PlayCommand()
)

fun executeCommand(commandName: String, commandArgs: String): String {
    return commands[commandName]?.execute(commandArgs)
            ?: "Command $commandName not found"
}