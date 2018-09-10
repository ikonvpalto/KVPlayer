package org.kvpbldsck.kvplayer.player.impl

import org.kvpbldsck.extensions.isGreaterThan
import org.kvpbldsck.extensions.isLessThan
import org.kvpbldsck.kvplayer.player.Volume
import javax.sound.sampled.FloatControl
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min

private const val MAX_VOLUME = 1.0f
private const val MIN_VOLUME = 0.0f
private const val DEFAULT_VOLUME = (MAX_VOLUME + MIN_VOLUME) / 2
private const val VOLUME_DELTA = (MAX_VOLUME - MIN_VOLUME) / 100
private const val PERCENTAGE_MULTIPLIER = 100
private const val MIN_PERCENTS = MIN_VOLUME * PERCENTAGE_MULTIPLIER
private const val MAX_PERCENTS = MAX_VOLUME * PERCENTAGE_MULTIPLIER

class VolumeImpl : Volume {

    var masterGain: FloatControl? = null
        set(value) {
            field = value
            setMasterGainValue()
        }

    private var volume: Float = DEFAULT_VOLUME
        set(value) {
            field = value
            setMasterGainValue()
        }

    override var percentage: Float
        get() {
            return volume.toPercents()
        }
        set(value) {
            assert(value.isPercents()) { "percentage must be between $MIN_PERCENTS and $MAX_PERCENTS" }
            volume = value.fromPercents()
        }

    override fun up() {
        volume = min(MAX_VOLUME, volume + VOLUME_DELTA)
    }

    override fun down() {
        volume = max(MIN_VOLUME, volume - VOLUME_DELTA)
    }

    private fun setMasterGainValue() {
        if (!masterGain.isNull()) {
            masterGain?.value = 20 * log10(volume)
        }
    }

    private fun Float.toPercents() = this * PERCENTAGE_MULTIPLIER
    private fun Float.fromPercents() = this / PERCENTAGE_MULTIPLIER
    private fun Float.isPercents() = (!MIN_PERCENTS.isGreaterThan(this)) && (!MAX_PERCENTS.isLessThan(this))
    private fun Any?.isNull() = null == this

}