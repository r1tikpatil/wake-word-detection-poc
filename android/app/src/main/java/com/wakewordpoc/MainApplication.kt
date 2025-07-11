package com.wakewordpoc

import android.app.Application
import android.content.*
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.soloader.SoLoader

class MainApplication : Application(), ReactApplication {

    private val wakeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val reactContext: ReactContext? = reactNativeHost.reactInstanceManager.currentReactContext
            reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                ?.emit("GarvisWake", null)
        }
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        val filter = IntentFilter("GARVIS_WAKEWORD")
        registerReceiver(wakeReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    override val reactNativeHost: ReactNativeHost = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport() = true
        override fun getPackages(): List<ReactPackage> = listOf(GarvisPackage())
        override fun getJSMainModuleName() = "index"
    }
}
