package org.kvpbldsck.kvplayer.player.impl

import kotlinx.coroutines.experimental.*
import org.kvpbldsck.kvplayer.player.Player
import org.kvpbldsck.kvplayer.exception.AudioPlayerException
import org.kvpbldsck.kvplayer.player.loader.AudioTrack
import java.nio.file.Path
import javax.sound.sampled.*
import kotlin.coroutines.experimental.Continuation

private const val AUDIO_CHUNK_SIZE = 128

class LocalTrackPlayer: Player {

    private var _isOpened = false
    private var _isPlaying = false

    override val isOpened
        get() = _isOpened
    override val isPlaying
        get() = _isPlaying

    private lateinit var audioTrack: AudioTrack
    private lateinit var audioOut: SourceDataLine

    private lateinit var playJob: Job
    private lateinit var playContinuation: Continuation<String>

    override fun open(track: Path) {
        if (_isOpened)
            throw AudioPlayerException("You should close already opened track before opening new one")

        audioTrack = AudioTrack.fromFilePath(track)

        openSpeakersOut()
        prepareForFirstPlay()

        _isOpened = true
    }

    override fun close() {
        if (!_isOpened)
            throw AudioPlayerException("You try to close track, but there is no opened one")

        stop()
        playJob.cancel()
        audioTrack.close()
        _isOpened = false
    }

    override fun play() {
        if (!_isOpened)
            throw AudioPlayerException("Nothing opened now")

        _isPlaying = true
        audioOut.start()
        playContinuation.resume("Play")
    }

    override fun pause() {
        if (!_isPlaying)
            throw AudioPlayerException("Nothing playing now")

        _isPlaying = false
        audioOut.stop()
    }

    override fun playPause() {
        if (!_isOpened)
            throw AudioPlayerException("There is no track opened")

        println(_isPlaying)

        when (_isPlaying) {
            true -> pause()
            false -> play()
        }
    }

    override fun stop() {
        _isPlaying = false
        audioOut.drain()
        audioOut.stop()
    }

    private fun openSpeakersOut() {
        audioOut = AudioSystem.getSourceDataLine(audioTrack.decodedAudioFormat)
        audioOut.open()
    }

    private fun prepareForFirstPlay() {
        playJob = launch(start = CoroutineStart.LAZY) {
            pausePlayCoroutine()
            prepareForPlaying()
            val audioChunk = ByteArray(AUDIO_CHUNK_SIZE)
            while (_isOpened) {

                if (!_isPlaying) {
                    println("Before pausing")
                    pausePlayCoroutine()
                    println("After pausing")
                    continue
                }

                val bytesRead = audioTrack.decodedIn.read(audioChunk)

                if (bytesRead > 0) {
                    audioOut.write(audioChunk, 0, bytesRead)
                } else {
                    stop()
                }
            }
        }

        playJob.start()
    }

    private suspend fun pausePlayCoroutine() = suspendCancellableCoroutine<String> {
        playContinuation = it
    }

    private fun prepareForPlaying() {
        if (!_isOpened)
            throw AudioPlayerException("Cannot play if no track opened")

        _isPlaying = true
        audioOut.start()
    }

}