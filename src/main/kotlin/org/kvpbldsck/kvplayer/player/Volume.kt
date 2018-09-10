package org.kvpbldsck.kvplayer.player

interface Volume {
    var percentage: Float

    fun up()
    fun down()
}