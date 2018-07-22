package org.kvpbldsck.kvplayer

import org.kvpbldsck.kvplayer.player.impl.LocalTrackPlayer
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var audioFilePath = parseArgs(args)

    var player = LocalTrackPlayer()
    player.open(Paths.get(audioFilePath))
    player.play()
}

fun parseArgs(args: Array<String>): String {
    if (1 != args.size) {
        printUsage()
        exitProcess(1)
    }

    return args[0]
}

fun printUsage() {
    println("Usage: kvplayer <path_to_audio_file>")
}
