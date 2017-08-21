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
package atu.com.dueroslib.devicemodule.voiceoutput.message;

import atu.com.dueroslib.framework.message.Payload;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * SpeechStarted和SpeechFinished事件对应的payload结构
 * <p>
 * Created by guxiuzhong@baidu.com on 2017/6/1.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SpeechLifecyclePayload extends Payload {
    private String token;

    public SpeechLifecyclePayload(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}