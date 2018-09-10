package org.kvpbldsck.kvplayer.player

interface PlaybackProgress {

    var percentage: Float
    val durationInSeconds : Int

    fun forward()
    fun baskward()

}