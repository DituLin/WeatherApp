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
package atu.com.dueroslib.framework.message;

import atu.com.dueroslib.devicemodule.alerts.message.DeleteAlertPayload;
import atu.com.dueroslib.devicemodule.alerts.message.SetAlertPayload;
import atu.com.dueroslib.devicemodule.audioplayer.message.ClearQueuePayload;
import atu.com.dueroslib.devicemodule.audioplayer.message.PlayPayload;
import atu.com.dueroslib.devicemodule.audioplayer.message.StopPayload;
import atu.com.dueroslib.devicemodule.screen.message.HtmlPayload;
import atu.com.dueroslib.devicemodule.speakcontroller.message.AdjustVolumePayload;
import atu.com.dueroslib.devicemodule.speakcontroller.message.SetMutePayload;
import atu.com.dueroslib.devicemodule.speakcontroller.message.SetVolumePayload;
import atu.com.dueroslib.devicemodule.system.message.SetEndPointPayload;
import atu.com.dueroslib.devicemodule.system.message.ThrowExceptionPayload;
import atu.com.dueroslib.devicemodule.voiceinput.message.ListenPayload;
import atu.com.dueroslib.devicemodule.voiceoutput.message.SpeakPayload;

import java.util.HashMap;

/**
 * 利用namespace和name做为key存储不同payload子类class并且find
 * <p>
 * Created by wuruisheng on 2017/6/1.
 */
public class PayloadConfig {
    private final HashMap<String, Class<?>> payloadClass;

    private static class PayloadConfigHolder {
        private static final PayloadConfig instance = new PayloadConfig();
    }

    public static PayloadConfig getInstance() {
        return PayloadConfigHolder.instance;
    }

    private PayloadConfig() {
        payloadClass = new HashMap<>();

        // AudioInputImpl
        String namespace = atu.com.dueroslib.devicemodule.voiceinput.ApiConstants.NAMESPACE;
        String name = atu.com.dueroslib.devicemodule.voiceinput.ApiConstants.Directives.Listen.NAME;
        insertPayload(namespace, name, ListenPayload.class);

        // VoiceOutput
        namespace = atu.com.dueroslib.devicemodule.voiceoutput.ApiConstants.NAMESPACE;
        name = atu.com.dueroslib.devicemodule.voiceoutput.ApiConstants.Directives.Speak.NAME;
        insertPayload(namespace, name, SpeakPayload.class);

        // audio
        namespace = atu.com.dueroslib.devicemodule.audioplayer.ApiConstants.NAMESPACE;
        name = atu.com.dueroslib.devicemodule.audioplayer.ApiConstants.Directives.Play.NAME;
        insertPayload(namespace, name, PlayPayload.class);

        name = atu.com.dueroslib.devicemodule.audioplayer.ApiConstants.Directives.Stop.NAME;
        insertPayload(namespace, name, StopPayload.class);

        name = atu.com.dueroslib.devicemodule.audioplayer.ApiConstants.Directives.ClearQueue.NAME;
        insertPayload(namespace, name, ClearQueuePayload.class);

        //  alert
        namespace = atu.com.dueroslib.devicemodule.alerts.ApiConstants.NAMESPACE;
        name = atu.com.dueroslib.devicemodule.alerts.ApiConstants.Directives.SetAlert.NAME;
        insertPayload(namespace, name, SetAlertPayload.class);

        name = atu.com.dueroslib.devicemodule.alerts.ApiConstants.Directives.DeleteAlert.NAME;
        insertPayload(namespace, name, DeleteAlertPayload.class);

        // SpeakController
        namespace = atu.com.dueroslib.devicemodule.speakcontroller.ApiConstants.NAMESPACE;
        name = atu.com.dueroslib.devicemodule.speakcontroller.ApiConstants.Directives.SetVolume.NAME;
        insertPayload(namespace, name, SetVolumePayload.class);

        name = atu.com.dueroslib.devicemodule.speakcontroller.ApiConstants.Directives.AdjustVolume.NAME;
        insertPayload(namespace, name, AdjustVolumePayload.class);

        name = atu.com.dueroslib.devicemodule.speakcontroller.ApiConstants.Directives.SetMute.NAME;
        insertPayload(namespace, name, SetMutePayload.class);

        // System
        namespace = atu.com.dueroslib.devicemodule.system.ApiConstants.NAMESPACE;
        name = atu.com.dueroslib.devicemodule.system.ApiConstants.Directives.SetEndpoint.NAME;
        insertPayload(namespace, name, SetEndPointPayload.class);

        name = atu.com.dueroslib.devicemodule.system.ApiConstants.Directives.ThrowException.NAME;
        insertPayload(namespace, name, ThrowExceptionPayload.class);

        // Screen
        namespace = atu.com.dueroslib.devicemodule.screen.ApiConstants.NAMESPACE;
        name = atu.com.dueroslib.devicemodule.screen.ApiConstants.Directives.HtmlView.NAME;
        insertPayload(namespace, name, HtmlPayload.class);
    }

    void insertPayload(String namespace, String name, Class<?> type) {
        final String key = namespace + name;
        payloadClass.put(key, type);
    }

    Class<?> findPayloadClass(String namespace, String name) {
        final String key = namespace + name;
        return payloadClass.get(key);
    }
}