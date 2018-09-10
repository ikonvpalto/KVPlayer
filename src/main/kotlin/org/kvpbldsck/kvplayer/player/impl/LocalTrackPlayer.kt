package org.kvpbldsck.kvplayer.player.impl

import kotlinx.coroutines.experimental.*
import org.kvpbldsck.kvplayer.player.Player
import org.kvpbldsck.kvplayer.player.loader.AudioTrack
import java.nio.file.Path
import javax.sound.sampled.*
import kotlin.coroutines.experimental.Continuation

private const val AUDIO_CHUNK_SIZE = 128

class LocalTrackPlayer: Player {

    private var _isOpened = false
    private var _isPlaying = false

    private lateinit var audioTrack: AudioTrack
    private lateinit var audioOut: SourceDataLine

    private lateinit var playJob: Job
    private lateinit var playContinuation: Continuation<String>

    override val isOpened
        get() = _isOpened
    override val isPlaying
        get() = _isPlaying
    override val volume = VolumeImpl()

    override fun open(track: Path) {
        assert(!_isOpened) { "You should close already opened track before opening new one" }

        audioTrack = AudioTrack.fromFilePath(track)

        openSpeakersOut()
        prepareForFirstPlay()

        _isOpened = true
    }

    override fun close() {
        assert(_isOpened) { "You try to close track, but there is no opened one" }

        stop()
        playJob.cancel()
        audioTrack.close()
        _isOpened = false
    }

    override fun play() {
        assert(_isOpened) { "Nothing opened now" }
        assert(!_isPlaying) { "Already play" }

        _isPlaying = true
        audioOut.start()
        playContinuation.resume("Play")
    }

    override fun pause() {
        assert(_isOpened) { "Nothing opened now" }
        assert(!_isPlaying) { "Nothing playing now" }

        _isPlaying = false
        audioOut.stop()
    }

    override fun playPause() {
        assert(_isOpened) { "There is no track opened" }

        when (_isPlaying) {
            true -> pause()
            false -> play()
        }
    }

    override fun stop() {
        assert(_isOpened) { "Nothing opened now" }

        pause()
    }

    private fun openSpeakersOut() {
        audioOut = AudioSystem.getSourceDataLine(audioTrack.decodedAudioFormat)
        audioOut.open()
        volume.masterGain = audioOut.getMasterGainControl()
    }

    private fun prepareForFirstPlay() {
        playJob = launch(start = CoroutineStart.LAZY) {
            pausePlayCoroutine()
            prepareForPlaying()
            val audioChunk = ByteArray(AUDIO_CHUNK_SIZE)
            while (_isOpened) {

                if (!_isPlaying) {
                    pausePlayCoroutine()
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
        assert(_isOpened) { "Cannot play if no track opened" }

        _isPlaying = true
        audioOut.start()
    }

    private fun SourceDataLine.getVolumeControl() = getControl(FloatControl.Type.VOLUME) as FloatControl
    private fun SourceDataLine.getMasterGainControl() = getControl(FloatControl.Type.MASTER_GAIN) as FloatControl

}