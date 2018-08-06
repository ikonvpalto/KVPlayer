package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.di.objectContainer
import org.kvpbldsck.kvplayer.exception.AudioPlayerException
import org.kvpbldsck.kvplayer.player.Player

class PauseCommand: Command {

    private val player = objectContainer.player

    override fun execute(args: String): String {
        if (!player.isPlaying) {
            return "Already paused"
        }

        return try {
            player.pause()
            "Paused"
        } catch (e: AudioPlayerException) {
            e.printStackTrace()
            e.message ?: "Cannot pause"
        }
    }

}