package org.kvpbldsck.kvplayer.ui.commands

import org.kvpbldsck.kvplayer.di.objectContainer
import org.kvpbldsck.kvplayer.exception.AudioPlayerException
import org.kvpbldsck.kvplayer.player.Player
import java.nio.file.Paths

class OpenCommand: Command {

    private val player: Player = objectContainer.player

    override fun execute(args: String): String {
        if (player.isOpened) {
            return "There is already opened track"
        }

        val audioFilePath = Paths.get(args.trim())

        return try {
            player.open(audioFilePath)
            "Opened"
        } catch (e: AudioPlayerException) {
            e.printStackTrace()
            e.message ?: "Cannot open track $audioFilePath"
        }
    }
}