package org.kvpbldsck.kvplayer.ui.commands

interface Command {

    fun execute(args: String): String
    val name: String
    val description: String

}