package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.di.objectContainer
import org.kvpbldsck.kvplayer.exception.AudioPlayerException
import org.kvpbldsck.kvplayer.player.Player

class PlayCommand: Command {

    private val player: Player = objectContainer.player

    override fun execute(args: String): String {
        if (player.isPlaying) {
            return "Already playing"
        }

        return try {
            player.play()
            "Success"
        } catch (e: AudioPlayerException) {
            e.printStackTrace()
            e.message ?: "Cannot play"
        }
    }

}