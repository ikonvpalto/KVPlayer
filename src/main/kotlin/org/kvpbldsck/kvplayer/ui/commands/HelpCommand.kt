package org.kvpbldsck.kvplayer.ui.commands

class HelpCommand: Command {
    override val name: String
        get() = "help"
    override val description: String
        get() = "Print help"

    override fun execute(args: String): String {
        return commandSet
                .asSequence()
                .map { "${it.name}: ${it.description}" }
                .joinToString("\n")
    }
}