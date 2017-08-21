package atu.com.weatherapp.dcs

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import atu.com.dueroslib.androidsystemimpl.PlatformFactoryImpl
import atu.com.dueroslib.devicemodule.voiceinput.VoiceInputDeviceModule
import atu.com.dueroslib.framework.DcsFramework
import atu.com.dueroslib.framework.DeviceModuleFactory
import atu.com.dueroslib.http.HttpConfig
import atu.com.dueroslib.wakeup.WakeUp
import org.jetbrains.anko.toast

class DcsService : Service() {

    private lateinit var platformFactory: PlatformFactoryImpl
    private lateinit var dcsFramework: DcsFramework
    private lateinit var deviceModuleFactory: DeviceModuleFactory
    private lateinit var wakeup: WakeUp
    private var isStopListenReceiving: Boolean = false

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }


    override fun onCreate() {
        super.onCreate()
        Log.e("DcsService","onCreate")
        initFramework()
    }

    /**
     * Framework
     */
    fun initFramework() {
        platformFactory = PlatformFactoryImpl(this)
        dcsFramework = DcsFramework(platformFactory)
        deviceModuleFactory = dcsFramework.deviceModuleFactory
        deviceModuleFactory.createVoiceOutputDeviceModule()
        deviceModuleFactory.createVoiceInputDeviceModule()
        deviceModuleFactory.createAlertsDeviceModule()
        deviceModuleFactory.createAudioPlayerDeviceModule()
        deviceModuleFactory.createSystemDeviceModule()
        deviceModuleFactory.createSpeakControllerDeviceModule()
        deviceModuleFactory.createPlaybackControllerDeviceModule()
        deviceModuleFactory.voiceInputDeviceModule.addVoiceInputListener(object : VoiceInputDeviceModule.IVoiceInputListener {
            override fun onStartRecord() {
                Log.e("onStartRecord","onStartRecord")
                startRecording()
            }

            override fun onFinishRecord() {
                Log.e("onFinishRecord","onFinishRecord")
                stopRecording()
            }

            override fun onSucceed(statusCode: Int) {
                Log.e("succeed",statusCode.toString())
                if (statusCode != 200) {
                    stopRecording()
                    toast("识别失败")
                }
            }

            override fun onFailed(errorMessage: String?) {
                stopRecording()
                toast("识别失败")
            }

        })

        wakeup = WakeUp(platformFactory.wakeUp, platformFactory.audioRecord)
        wakeup.addWakeUpListener {
            toast("喵！主人")
            startVoice()
        }
        wakeup.startWakeUp()
    }

    fun startRecording() {
        wakeup.stopWakeUp()
        isStopListenReceiving = true
        deviceModuleFactory.systemProvider.userActivity()

    }

    fun stopRecording() {
        wakeup.startWakeUp()
        isStopListenReceiving = false
    }


    fun startVoice() {
        if (isStopListenReceiving) {
            platformFactory.voiceInput.stopRecord()
            isStopListenReceiving = false
            return
        }
        isStopListenReceiving = true
        platformFactory.voiceInput.startRecord()
        deviceModuleFactory.systemProvider.userActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("DcsService","onDestroy")
        wakeup.removeWakeUpListener { }
        wakeup.stopWakeUp()
        wakeup.releaseWakeUp()
        dcsFramework.release()
    }

}
