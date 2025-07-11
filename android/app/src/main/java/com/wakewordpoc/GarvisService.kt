package com.wakewordpoc
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ai.onnxruntime.*
import java.io.File
import java.nio.FloatBuffer
import kotlin.concurrent.thread
import com.wakewordpoc.R


class GarvisService : Service() {
    companion object {
        const val NOTIF_ID = 1337
        const val CHANNEL_ID = "GarvisChannel"
    }

    private var running = true
    private lateinit var ortSession: OrtSession
    private lateinit var ortEnv: OrtEnvironment

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Garvis listening…")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now) // Built-in icon
            .setOngoing(true)
            .build()

        thread {
            initOnnx()
            runAudioCapture()
        }
    }

    private fun initOnnx() {
        ortEnv = OrtEnvironment.getEnvironment()
        val modelInput = assets.open("hey_garvis.onnx").readBytes()
        ortSession = ortEnv.createSession(modelInput)
    }

    private fun runAudioCapture() {
        val sampleRate = 16000
        val bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT)
        val recorder = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize)
        val audioBuffer = ShortArray(bufferSize)
        recorder.startRecording()

        while (running) {
            val read = recorder.read(audioBuffer, 0, bufferSize)
            if (read > 0) {
                val floatInput = FloatArray(read) { i -> audioBuffer[i] / 32768.0f }
                val inputTensor = OnnxTensor.createTensor(ortEnv, arrayOf(floatInput))
                val result = ortSession.run(mapOf("input" to inputTensor))
                val output = (result[0].value as Array<FloatArray>)[0][0]
                if (output > 0.7f) {
                    sendBroadcast(Intent("GARVIS_WAKEWORD"))
                }
            }
        }
        recorder.stop()
        recorder.release()
    }

    override fun onDestroy() {
        running = false
        ortSession.close()
        ortEnv.close()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val chan = NotificationChannel(CHANNEL_ID, "Garvis", NotificationManager.IMPORTANCE_LOW)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(chan)
    }
}
