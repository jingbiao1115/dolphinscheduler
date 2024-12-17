/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.plugin.alert.wechat;

public enum WeChatType {

	APP(1, "APP/应用"), APPCHAT(2, "GROUP CHAT/群聊"),;

	private final int code;
	private final String descp;

	WeChatType(int code, String descp) {
		this.code = code;
		this.descp = descp;
	}

	public int getCode() {
		return code;
	}

	public String getDescp() {
		return descp;
	}
}