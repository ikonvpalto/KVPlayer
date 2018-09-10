package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.di.objectContainer
import org.kvpbldsck.kvplayer.exception.AudioPlayerException

class PlayPauseCommand: Command {
    override val name: String
        get() = "playPause"
    override val description: String
        get() = "Play or pause current track"

    private val player = objectContainer.player

    override fun execute(args: String): String {
        return try {
            player.playPause()
            if (player.isPlaying)
                "Playing"
            else
                "Paused"
        } catch (e: AudioPlayerException) {
            e.printStackTrace()
            e.message ?: "Cannot play"
        }
    }
}