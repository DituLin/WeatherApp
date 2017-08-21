/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package atu.com.dueroslib.framework;

import atu.com.dueroslib.devicemodule.alerts.AlertsDeviceModule;
import atu.com.dueroslib.devicemodule.audioplayer.AudioPlayerDeviceModule;
import atu.com.dueroslib.devicemodule.playbackcontroller.PlaybackControllerDeviceModule;
import atu.com.dueroslib.devicemodule.screen.ScreenDeviceModule;
import atu.com.dueroslib.devicemodule.speakcontroller.SpeakerControllerDeviceModule;
import atu.com.dueroslib.devicemodule.system.SystemDeviceModule;
import atu.com.dueroslib.devicemodule.system.message.SetEndPointPayload;
import atu.com.dueroslib.devicemodule.system.message.ThrowExceptionPayload;
import atu.com.dueroslib.devicemodule.voiceinput.VoiceInputDeviceModule;
import atu.com.dueroslib.devicemodule.voiceoutput.VoiceOutputDeviceModule;
import atu.com.dueroslib.http.HttpConfig;
import atu.com.dueroslib.systeminterface.IMediaPlayer;
import atu.com.dueroslib.systeminterface.IPlatformFactory;
import atu.com.dueroslib.systeminterface.IPlaybackController;
import atu.com.dueroslib.systeminterface.IWebView;
import atu.com.dueroslib.util.LogUtil;

/**
 * 创建语音输入、语音输出、扬声器、音频播放器、播放控制、闹钟、屏幕显示和系统等deviceModule
 * <p>
 * Created by wuruisheng on 2017/6/15.
 */
public class DeviceModuleFactory {
    private static final String TAG = DeviceModuleFactory.class.getSimpleName();
    private final IDeviceModuleHandler deviceModuleHandler;
    private final IMediaPlayer dialogMediaPlayer;

    private VoiceInputDeviceModule voiceInputDeviceModule;
    private VoiceOutputDeviceModule voiceOutputDeviceModule;
    private SpeakerControllerDeviceModule speakerControllerDeviceModule;
    private AudioPlayerDeviceModule audioPlayerDeviceModule;
    private AlertsDeviceModule alertsDeviceModule;
    private SystemDeviceModule systemDeviceModule;
    private PlaybackControllerDeviceModule playbackControllerDeviceModule;
    private ScreenDeviceModule screenDeviceModule;

    // 数字越大，优先级越高，播放优先级
    private enum MediaChannel {
        SPEAK("dialog", 3),
        ALERT("alert", 2),
        AUDIO("audio", 1);

        private String channelName;
        private int priority;

        MediaChannel(String channelName, int priority) {
            this.channelName = channelName;
            this.priority = priority;
        }
    }

    public DeviceModuleFactory(final IDeviceModuleHandler deviceModuleHandler) {
        this.deviceModuleHandler = deviceModuleHandler;
        dialogMediaPlayer = deviceModuleHandler.getMultiChannelMediaPlayer()
                .addNewChannel(MediaChannel.SPEAK.channelName, MediaChannel.SPEAK.priority);
    }


    public void createVoiceInputDeviceModule() {
        /*
         * 传入VoiceOutput的MediaPlayer，因为根据dcs协议的规范
         * 对话通道：
         * 对应语音输入（Voice Input）和语音输出（Voice Output）端能力；
         * 用户在语音请求时，或者设备在执行Speak指令进行播报时，对话通道进入活跃状态
         */
        voiceInputDeviceModule = new VoiceInputDeviceModule(
                dialogMediaPlayer, deviceModuleHandler.getMessageSender(),
                deviceModuleHandler.getPlatformFactory().getVoiceInput(),
                deviceModuleHandler.getDialogRequestIdHandler(),
                deviceModuleHandler.getResponseDispatcher());
        deviceModuleHandler.addDeviceModule(voiceInputDeviceModule);
    }

    public VoiceInputDeviceModule getVoiceInputDeviceModule() {
        return voiceInputDeviceModule;
    }

    public void createVoiceOutputDeviceModule() {
        voiceOutputDeviceModule = new VoiceOutputDeviceModule(dialogMediaPlayer,
                deviceModuleHandler.getMessageSender());
        voiceOutputDeviceModule.addVoiceOutputListener(new VoiceOutputDeviceModule.IVoiceOutputListener() {
            @Override
            public void onVoiceOutputStarted() {
                LogUtil.d(TAG, "DcsResponseBodyEnqueue-onVoiceOutputStarted ok ");
                deviceModuleHandler.getResponseDispatcher().blockDependentQueue();
            }

            @Override
            public void onVoiceOutputFinished() {
                LogUtil.d(TAG, "DcsResponseBodyEnqueue-onVoiceOutputFinished ok ");
                deviceModuleHandler.getResponseDispatcher().unBlockDependentQueue();
            }
        });

        deviceModuleHandler.addDeviceModule(voiceOutputDeviceModule);
    }

    public void createSpeakControllerDeviceModule() {
        BaseMultiChannelMediaPlayer.ISpeakerController speakerController =
                deviceModuleHandler.getMultiChannelMediaPlayer().getSpeakerController();
        speakerControllerDeviceModule =
                new SpeakerControllerDeviceModule(speakerController,
                        deviceModuleHandler.getMessageSender());
        deviceModuleHandler.addDeviceModule(speakerControllerDeviceModule);
    }

    public void createAudioPlayerDeviceModule() {
        IMediaPlayer mediaPlayer = deviceModuleHandler.getMultiChannelMediaPlayer()
                .addNewChannel(MediaChannel.AUDIO.channelName,
                        MediaChannel.AUDIO.priority);
        audioPlayerDeviceModule = new AudioPlayerDeviceModule(mediaPlayer,
                deviceModuleHandler.getMessageSender());
        deviceModuleHandler.addDeviceModule(audioPlayerDeviceModule);
    }

    public AudioPlayerDeviceModule getAudioPlayerDeviceModule() {
        return audioPlayerDeviceModule;
    }

    public void createAlertsDeviceModule() {
        IMediaPlayer mediaPlayer = deviceModuleHandler.getMultiChannelMediaPlayer()
                .addNewChannel(MediaChannel.ALERT.channelName,
                        MediaChannel.ALERT.priority);
        alertsDeviceModule = new AlertsDeviceModule(mediaPlayer,
                deviceModuleHandler.getPlatformFactory().createAlertsDataStore(),
                deviceModuleHandler.getMessageSender(),
                deviceModuleHandler.getPlatformFactory().getMainHandler());

        alertsDeviceModule.addAlertListener(new AlertsDeviceModule.IAlertListener() {
            @Override
            public void onAlertStarted(String alertToken) {
            }
        });

        deviceModuleHandler.addDeviceModule(alertsDeviceModule);
    }

    public void createSystemDeviceModule() {
        systemDeviceModule = new SystemDeviceModule(deviceModuleHandler.getMessageSender());
        systemDeviceModule.addModuleListener(new SystemDeviceModule.IDeviceModuleListener() {
            @Override
            public void onSetEndpoint(SetEndPointPayload endPointPayload) {
                if (null != endPointPayload) {
                    String endpoint = endPointPayload.getEndpoint();
                    if (null != endpoint && endpoint.length() > 0) {
                        HttpConfig.setEndpoint(endpoint);
                    }
                }
            }

            @Override
            public void onThrowException(ThrowExceptionPayload throwExceptionPayload) {
                LogUtil.v(TAG, throwExceptionPayload.toString());
            }
        });
        deviceModuleHandler.addDeviceModule(systemDeviceModule);
    }

    public SystemDeviceModule getSystemDeviceModule() {
        return systemDeviceModule;
    }

    public SystemDeviceModule.Provider getSystemProvider() {
        return systemDeviceModule.getProvider();
    }

    public void createPlaybackControllerDeviceModule() {
        IPlaybackController playback = deviceModuleHandler.getPlatformFactory().getPlayback();
        playbackControllerDeviceModule = new PlaybackControllerDeviceModule(playback,
                deviceModuleHandler.getMessageSender(), alertsDeviceModule);
        deviceModuleHandler.addDeviceModule(playbackControllerDeviceModule);
    }

    public void createScreenDeviceModule() {
        IWebView webView = deviceModuleHandler.getPlatformFactory().getWebView();
        screenDeviceModule = new ScreenDeviceModule(webView, deviceModuleHandler.getMessageSender());
        deviceModuleHandler.addDeviceModule(screenDeviceModule);
    }

    public interface IDeviceModuleHandler {
        IPlatformFactory getPlatformFactory();

        DialogRequestIdHandler getDialogRequestIdHandler();

        IMessageSender getMessageSender();

        BaseMultiChannelMediaPlayer getMultiChannelMediaPlayer();

        void addDeviceModule(BaseDeviceModule deviceModule);

        DcsResponseDispatcher getResponseDispatcher();
    }
}