package org.kvpbldsck.kvplayer.player

import java.nio.file.Path

interface Player {

    val isPlaying: Boolean
    val isOpened: Boolean

    fun open(track: Path)
    fun close()
    fun play()
    fun pause()
    fun playPause()
    fun stop()

}