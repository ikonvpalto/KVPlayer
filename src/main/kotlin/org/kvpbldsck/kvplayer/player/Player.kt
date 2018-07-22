package org.kvpbldsck.kvplayer.player

import java.nio.file.Path

interface Player {

    fun open(track: Path)
    fun close()
    fun play()
    fun stop()

}