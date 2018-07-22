package org.kvpbldsck.kvplayer.player.impl

import org.kvpbldsck.kvplayer.player.Player
import org.kvpbldsck.kvplayer.exception.AudioPlayerException
import java.nio.file.Files
import java.nio.file.Path
import javax.sound.sampled.*

private const val AUDIO_CHUNK_SIZE = 128

class LocalTrackPlayer: Player {

    private var _isOpened = false
    private var _isPlaying = false

    override val isOpened
        get() = _isOpened
    override val isPlaying
        get() = _isPlaying

    private lateinit var filePath: Path
    private lateinit var fileFormat: AudioFileFormat
    private lateinit var encodedIn: AudioInputStream
    private lateinit var decodedIn: AudioInputStream
    private lateinit var encodedAudioFormat: AudioFormat
    private lateinit var decodedAudioFormat: AudioFormat
    private lateinit var audioOut: SourceDataLine

    override fun open(track: Path) {
        ensureCanOpen()
        track.ensureCanPlayFile()

        openAudioFile(track)
        openDecodedStream()
        openOutStream()

        filePath = track

        _isOpened = true
    }

    private fun ensureCanOpen() {
        if (_isOpened)
            throw AudioPlayerException("You should close already opened track before opening new one")
    }

    private fun Path.ensureCanPlayFile() {
        if (!Files.exists(this))
            throw AudioPlayerException("File $this not found")
    }

    private fun openAudioFile(track: Path) {
        fileFormat = AudioSystem.getAudioFileFormat(track.toFile())
        encodedIn = AudioSystem.getAudioInputStream(track.toFile())
        encodedAudioFormat = encodedIn.format
    }

    private fun openDecodedStream() {
        decodedAudioFormat = AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                encodedAudioFormat.sampleRate,
                16,
                encodedAudioFormat.channels,
                encodedAudioFormat.channels * 2,
                encodedAudioFormat.sampleRate,
                false)

        decodedIn = AudioSystem.getAudioInputStream(decodedAudioFormat, encodedIn)
    }

    private fun openOutStream() {
        audioOut = AudioSystem.getSourceDataLine(decodedAudioFormat)
        audioOut.open()
    }

    override fun close() {
        if (!_isOpened)
            throw AudioPlayerException("You try to close track, but there is no opened one")

        stop()

        decodedIn.close()
        encodedIn.close()

        _isOpened = false
    }

    override fun play() {
        prepareForPlaying()
        var audioChunk = ByteArray(AUDIO_CHUNK_SIZE)

        while (_isPlaying) {
            var bytesRead = decodedIn.read(audioChunk)

            if (bytesRead > 0) {
                audioOut.write(audioChunk, 0, bytesRead)
            } else {
                stop()
            }
        }
    }

    private fun prepareForPlaying() {
        if (!_isOpened)
            throw AudioPlayerException("Cannot play if no track opened")

        _isPlaying = true
        audioOut.start()
    }

    override fun stop() {
        _isPlaying = false
        audioOut.drain()
        audioOut.stop()
    }
}