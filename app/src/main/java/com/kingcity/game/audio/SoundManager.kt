package com.kingcity.game.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import java.util.Random
import kotlin.math.PI
import kotlin.math.sin

class SoundManager {

    private val sampleRate = 44100

    fun playCoin() = playTone(frequency = 880f, durationMs = 80)
    fun playClick() = playTone(frequency = 520f, durationMs = 55)
    fun playUnlock() = playChime()
    fun playShoot() = playNoiseBurst(durationMs = 90, gain = 0.4)
    fun playBusted() = playNoiseBurst(durationMs = 260, gain = 0.35)
    fun playSiren() = playSirenSweep()

    private fun playTone(frequency: Float, durationMs: Int) {
        try {
            val numSamples = (sampleRate * durationMs / 1000.0).toInt()
            val samples = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val t = i / sampleRate.toDouble()
                val amplitude = 1.0 - (i.toDouble() / numSamples)
                val value = sin(2.0 * PI * frequency * t) * amplitude * Short.MAX_VALUE * 0.6
                samples[i] = value.toInt().toShort()
            }
            playSamples(samples)
        } catch (e: Exception) {
        }
    }

    private fun playChime() {
        try {
            val freqs = listOf(660f, 880f, 990f, 1320f)
            val perNoteMs = 80
            val samplesPerNote = (sampleRate * perNoteMs / 1000.0).toInt()
            val samples = ShortArray(samplesPerNote * freqs.size)
            var offset = 0
            for (f in freqs) {
                for (i in 0 until samplesPerNote) {
                    val t = i / sampleRate.toDouble()
                    val amplitude = 1.0 - (i.toDouble() / samplesPerNote)
                    val value = sin(2.0 * PI * f * t) * amplitude * Short.MAX_VALUE * 0.6
                    samples[offset + i] = value.toInt().toShort()
                }
                offset += samplesPerNote
            }
            playSamples(samples)
        } catch (e: Exception) {
        }
    }

    private fun playNoiseBurst(durationMs: Int, gain: Double) {
        try {
            val numSamples = (sampleRate * durationMs / 1000.0).toInt()
            val samples = ShortArray(numSamples)
            val random = Random()
            for (i in 0 until numSamples) {
                val amplitude = 1.0 - (i.toDouble() / numSamples)
                val noise = random.nextDouble() * 2.0 - 1.0
                val value = noise * amplitude * Short.MAX_VALUE * gain
                samples[i] = value.toInt().toShort()
            }
            playSamples(samples)
        } catch (e: Exception) {
        }
    }

    private fun playSirenSweep() {
        try {
            val durationMs = 500
            val numSamples = (sampleRate * durationMs / 1000.0).toInt()
            val samples = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val t = i / sampleRate.toDouble()
                val sweep = 700.0 + 500.0 * sin(2.0 * PI * 2.0 * t)
                val amplitude = 0.5
                val value = sin(2.0 * PI * sweep * t) * amplitude * Short.MAX_VALUE * 0.5
                samples[i] = value.toInt().toShort()
            }
            playSamples(samples)
        } catch (e: Exception) {
        }
    }

    private fun playSamples(samples: ShortArray) {
        val bufferSizeBytes = samples.size * 2
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSizeBytes)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(samples, 0, samples.size)
        audioTrack.setNotificationMarkerPosition(samples.size)
        audioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
            override fun onMarkerReached(track: AudioTrack) {
                track.release()
            }
            override fun onPeriodicNotification(track: AudioTrack) {}
        })
        audioTrack.play()
    }
}
