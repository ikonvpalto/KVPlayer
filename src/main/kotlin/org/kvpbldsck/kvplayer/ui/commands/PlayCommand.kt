package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.di.objectContainer
import org.kvpbldsck.kvplayer.exception.AudioPlayerException
import org.kvpbldsck.kvplayer.player.Player

class PlayCommand: Command {
    override val name: String
        get() = "play"
    override val description: String
        get() = "Play paused track"

    private val player: Player = objectContainer.player

    override fun execute(args: String): String {
        if (player.isPlaying) {
            return "Already playing"
        }

        return try {
            player.play()
            "Playing"
        } catch (e: AudioPlayerException) {
            e.printStackTrace()
            e.message ?: "Cannot play"
        }
    }

}