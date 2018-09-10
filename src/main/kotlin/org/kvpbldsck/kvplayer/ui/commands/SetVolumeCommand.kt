package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.di.objectContainer

class SetVolumeCommand: Command {
    override val name: String
        get() = "setVolume"
    override val description: String
        get() = "Set volume level in percents"

    val player = objectContainer.player

    override fun execute(args: String): String {
        val volume = args.toFloat()
        try {
            player.volume.percentage = volume
        } catch (e: Exception) {
            return e.message ?: "Cannot set volume to $args value"
        }
        return "Success"
    }
}