package com.example.mcperf

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private val native = NativeBridge
    private lateinit var settingsMgr: SettingsManager

    @SuppressLint("AddJavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        settingsMgr = SettingsManager(this)

        webView.apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
            addJavascriptInterface(AndroidBridge(), "Android")
            loadUrl("file:///android_asset/ui/index.html")
        }

        native.initLayer()

        lifecycleScope.launch {
            loadSettings()
            startFpsUpdater()
        }
    }

    private suspend fun loadSettings() {
        val prefs = settingsMgr.prefs.first()
        val resScale = prefs[SettingsManager.KEY_RES_SCALE] ?: 1.0f
        val fpsCap = prefs[SettingsManager.KEY_FPS_CAP] ?: 0
        val reduceEffects = prefs[SettingsManager.KEY_REDUCE_EFFECTS] ?: false
        val cpuOpt = prefs[SettingsManager.KEY_CPU_OPT] ?: false

        native.setResolutionScale(resScale)
        native.setFpsCap(fpsCap)
        native.setReduceEffects(reduceEffects)
        native.setCpuOptimization(cpuOpt)

        runOnUiThread {
            val deviceInfo = buildDeviceInfo()
            val script = """
                window.updateFromNative({
                    resolutionScale: $resScale,
                    fpsCap: $fpsCap,
                    reduceEffects: $reduceEffects,
                    cpuOptimization: $cpuOpt
                });
                window.updateDeviceInfo('$deviceInfo');
            """.trimIndent()
            webView.evaluateJavascript(script, null)
        }
    }

    private fun buildDeviceInfo(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val device = Build.DEVICE
        val sdk = Build.VERSION.SDK_INT
        return "$manufacturer $model ($device) - Android $sdk"
    }

    private fun startFpsUpdater() {
        lifecycleScope.launch {
            while (true) {
                val fps = native.getCurrentFps()
                webView.evaluateJavascript("window.setFps($fps);", null)
                delay(1000)
            }
        }
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun setResolutionScale(value: Float) {
            lifecycleScope.launch {
                settingsMgr.updateResolutionScale(value)
                native.setResolutionScale(value)
            }
        }

        @JavascriptInterface
        fun setFpsCap(value: Int) {
            lifecycleScope.launch {
                settingsMgr.updateFpsCap(value)
                native.setFpsCap(value)
            }
        }

        @JavascriptInterface
        fun setReduceEffects(enabled: Boolean) {
            lifecycleScope.launch {
                settingsMgr.updateReduceEffects(enabled)
                native.setReduceEffects(enabled)
            }
        }

        @JavascriptInterface
        fun setCpuOptimization(enabled: Boolean) {
            lifecycleScope.launch {
                settingsMgr.updateCpuOptimization(enabled)
                native.setCpuOptimization(enabled)
            }
        }

        @JavascriptInterface
        fun launchMinecraft() {
            try {
                val launchIntent = packageManager.getLaunchIntentForPackage("com.zalithlauncher")
                launchIntent?.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(launchIntent)
            } catch (e: Exception) {
                webView.evaluateJavascript("alert('Launcher not found. Please install Zalith Launcher.');", null)
            }
        }
    }
}