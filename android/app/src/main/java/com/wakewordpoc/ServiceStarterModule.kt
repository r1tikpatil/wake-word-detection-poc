package com.wakewordpoc
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import com.facebook.react.bridge.*

class ServiceStarterModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "ServiceStarter"

    @ReactMethod
    fun startGarvisService() {
        val intent = Intent(reactContext, GarvisService::class.java)
        startForegroundService(reactContext, intent)
    }
}