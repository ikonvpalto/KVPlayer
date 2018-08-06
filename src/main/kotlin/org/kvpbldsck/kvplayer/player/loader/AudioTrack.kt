package org.kvpbldsck.kvplayer.player.loader

import org.kvpbldsck.kvplayer.exception.AudioPlayerException
import java.io.Closeable
import java.nio.file.Files
import java.nio.file.Path
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

class AudioTrack private constructor(
    val fileFormat: AudioFileFormat,
    val audioIn: AudioInputStream,
    val audioFormat: AudioFormat
) : Closeable {

    companion object {

        fun fromFilePath(path: Path): AudioTrack {
            path.ensureAudioFileExists()

            val fileFormat = AudioSystem.getAudioFileFormat(path.toFile())
            val encodedIn = AudioSystem.getAudioInputStream(path.toFile())
            val audioFormat = encodedIn.format

            return AudioTrack(fileFormat, encodedIn, audioFormat)
        }

        private fun Path.ensureAudioFileExists() {
            if (!Files.exists(this))
                throw AudioPlayerException("File $this not found")
        }

    }

    val decodedIn: AudioInputStream by lazy {
        AudioSystem.getAudioInputStream(decodedAudioFormat, audioIn)
    }

    val decodedAudioFormat: AudioFormat by lazy {
        AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                audioFormat.sampleRate,
                16,
                audioFormat.channels,
                audioFormat.channels * 2,
                audioFormat.sampleRate,
                false)
    }

    override fun close() {
        try {
            if ((::decodedAudioFormat.delegateAs<Lazy<*>>())?.isInitialized() == true) {
                decodedIn.close()
            }
        } catch (e : Exception) {
            println("Cannot close decodedIn: ${e.message}")
            e.printStackTrace()
        }
        try {
            audioIn.close()
        } catch (e : Exception) {
            println("Cannot close audioIn: ${e.message}")
            e.printStackTrace()
        }
    }
}


inline fun <reified R> KProperty0<*>.delegateAs(): R? {
    isAccessible = true
    return getDelegate() as? R
}