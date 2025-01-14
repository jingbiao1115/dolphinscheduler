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

package org.apache.dolphinscheduler.plugin.task.remoteshell;


import lombok.Data;
import org.apache.dolphinscheduler.plugin.task.api.enums.ResourceType;
import org.apache.dolphinscheduler.plugin.task.api.parameters.AbstractParameters;
import org.apache.dolphinscheduler.plugin.task.api.parameters.resource.ResourceParametersHelper;

@Data
public class RemoteShellParameters extends AbstractParameters {

	private String rawScript;

	private String type;

	/**
	 * datasource id
	 */
	private int datasource;

	@Override
	public boolean checkParameters() {
		return rawScript != null && !rawScript.isEmpty();
	}

	@Override
	public ResourceParametersHelper getResources() {
		ResourceParametersHelper resources = super.getResources();
		resources.put(ResourceType.DATASOURCE, datasource);
		return resources;
	}

}
