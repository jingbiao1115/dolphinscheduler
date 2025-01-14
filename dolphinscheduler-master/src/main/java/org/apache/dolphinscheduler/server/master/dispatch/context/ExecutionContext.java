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

package org.apache.dolphinscheduler.server.master.dispatch.context;

import static org.apache.dolphinscheduler.common.constants.Constants.DEFAULT_WORKER_GROUP;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.dolphinscheduler.dao.entity.TaskInstance;
import org.apache.dolphinscheduler.extract.base.utils.Host;
import org.apache.dolphinscheduler.server.master.dispatch.enums.ExecutorType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionContext {

	private Host host;

	private TaskInstance taskInstance;

	private ExecutorType executorType;

	/**
	 * worker group
	 */
	private String workerGroup;

	public ExecutionContext(ExecutorType executorType, TaskInstance taskInstance) {
		this(executorType, DEFAULT_WORKER_GROUP, taskInstance);
	}

	public ExecutionContext(ExecutorType executorType, String workerGroup, TaskInstance taskInstance) {
		this.executorType = executorType;
		this.workerGroup = workerGroup;
		this.taskInstance = taskInstance;
	}
}
