package org.kvpbldsck.kvplayer.ui

import org.kvpbldsck.kvplayer.extensions.normalize
import org.kvpbldsck.kvplayer.ui.commands.executeCommand

class UserController {

    fun start() {
        var command = readCommand()

        while (!command.isExitCommand()) {
            try {
                val result = executeCommand(command.first, command.second)
                println(result)
            } catch (e : Exception) {
                println(e.localizedMessage)
                e.printStackTrace()
            }

            command = readCommand()
        }
    }

    private fun readCommand(): Pair<String, String> {
        System.out.flush()
        val line = readLine()

        if (null == line)
            throw RuntimeException("Cannot read user input")
        else {
            val splitPos = line.indexOfFirst { c -> c.isWhitespace() }

            val commandName: String
            val commandArgs: String

            if (-1 != splitPos) {
                commandName = line.substring(0 until splitPos).normalize()
                commandArgs = line.substring((splitPos + 1)..line.lastIndex).trim()
            } else {
                commandName = line
                commandArgs = ""
            }

            return Pair(commandName, commandArgs)
        }
    }

    private fun Pair<String, String>.isExitCommand() = "exit" == first

}