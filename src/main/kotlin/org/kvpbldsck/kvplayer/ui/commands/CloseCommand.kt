package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.di.objectContainer
import org.kvpbldsck.kvplayer.exception.AudioPlayerException
import org.kvpbldsck.kvplayer.player.Player

class CloseCommand: Command {

    val player: Player = objectContainer.player

    override fun execute(args: String): String {
        if (!player.isOpened) {
            return "No track opened"
        }

        return try
        {
            player.close()
            "Closed"
        } catch (e: AudioPlayerException)
        {
            e.printStackTrace()
            e.message ?: "Cannot close track"
        }
    }
}