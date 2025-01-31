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

package org.apache.dolphinscheduler.plugin.task.api.loop.template.http.parser;


import java.util.Map;
import lombok.NonNull;
import org.apache.dolphinscheduler.plugin.task.api.loop.template.LoopTaskYamlDefinition;
import org.apache.dolphinscheduler.plugin.task.api.loop.template.TemplateMethodTransformer;
import org.apache.dolphinscheduler.plugin.task.api.loop.template.http.method.HttpLoopTaskSubmitTaskMethodDefinition;

public class SubmitTemplateMethodTransformer
		implements
			TemplateMethodTransformer<LoopTaskYamlDefinition.LoopTaskSubmitMethodYamlDefinition, HttpLoopTaskSubmitTaskMethodDefinition> {

	@Override
	public @NonNull HttpLoopTaskSubmitTaskMethodDefinition transform(
			@NonNull LoopTaskYamlDefinition.LoopTaskSubmitMethodYamlDefinition loopTaskAPIYamlDefinition) {
		String url = loopTaskAPIYamlDefinition.getUrl();
		String method = loopTaskAPIYamlDefinition.getMethod();
		String dataType = loopTaskAPIYamlDefinition.getDataType();
		Map<String, String> httpHeaders = loopTaskAPIYamlDefinition.getHttpHeaders();
		Map<String, Object> requestParams = loopTaskAPIYamlDefinition.getRequestParams();
		Map<String, Object> requestBody = loopTaskAPIYamlDefinition.getRequestBody();
		String taskInstanceIdJPath = loopTaskAPIYamlDefinition.getTaskInstanceIdJPath();
		return new HttpLoopTaskSubmitTaskMethodDefinition(url, method, dataType, httpHeaders, requestParams,
				requestBody, taskInstanceIdJPath);
	}
}
