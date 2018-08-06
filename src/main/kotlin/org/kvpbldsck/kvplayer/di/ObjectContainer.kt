package org.kvpbldsck.kvplayer.di

import org.kvpbldsck.kvplayer.player.Player
import org.kvpbldsck.kvplayer.player.impl.LocalTrackPlayer

interface ObjectContainer {
    val player: Player
}

val objectContainer: ObjectContainer = object: ObjectContainer {

    override val player: Player = LocalTrackPlayer()

}

