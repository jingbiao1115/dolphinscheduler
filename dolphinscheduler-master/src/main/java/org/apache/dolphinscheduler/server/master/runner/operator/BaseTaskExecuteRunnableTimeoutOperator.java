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

package org.apache.dolphinscheduler.server.master.runner.operator;


import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.dolphinscheduler.dao.entity.TaskInstance;
import org.apache.dolphinscheduler.dao.repository.TaskInstanceDao;
import org.apache.dolphinscheduler.plugin.task.api.enums.TaskExecutionStatus;
import org.apache.dolphinscheduler.plugin.task.api.enums.TaskTimeoutStrategy;
import org.apache.dolphinscheduler.server.master.runner.DefaultTaskExecuteRunnable;

@Slf4j
public abstract class BaseTaskExecuteRunnableTimeoutOperator implements TaskExecuteRunnableOperator {

	private TaskInstanceDao taskInstanceDao;

	public BaseTaskExecuteRunnableTimeoutOperator(TaskInstanceDao taskInstanceDao) {
		this.taskInstanceDao = taskInstanceDao;
	}

	@Override
	public void operate(DefaultTaskExecuteRunnable taskExecuteRunnable) {
		// Right now, if the task is running in worker, the timeout strategy will be
		// handled at worker side.
		// if the task is in master, the timeout strategy will be handled at master
		// side.
		// todo: we should unify this, the master only need to handle the timeout
		// strategy. and send request to worker
		// to kill the task, if the strategy is timeout_failed.
		TaskInstance taskInstance = taskExecuteRunnable.getTaskInstance();
		TaskTimeoutStrategy taskTimeoutStrategy = taskInstance.getTaskDefine().getTimeoutNotifyStrategy();
		if (TaskTimeoutStrategy.FAILED != taskTimeoutStrategy
				&& TaskTimeoutStrategy.WARNFAILED != taskTimeoutStrategy) {
			log.warn("TaskInstance: {} timeout, the current timeout strategy is {}, will continue running",
					taskInstance.getName(), taskTimeoutStrategy.name());
			return;
		}
		try {
			timeoutTaskInstanceInDB(taskInstance);
			killRemoteTaskInstanceInThreadPool(taskInstance);
			log.info("TaskInstance: {} timeout, killed the task instance", taskInstance.getName());
		} catch (Exception ex) {
			log.error("TaskInstance timeout {} failed", taskInstance.getName(), ex);
		}

	}

	protected abstract void killRemoteTaskInstanceInThreadPool(TaskInstance taskInstance);

	private void timeoutTaskInstanceInDB(TaskInstance taskInstance) {
		taskInstance.setState(TaskExecutionStatus.FAILURE);
		taskInstance.setEndTime(new Date());
		taskInstanceDao.updateById(taskInstance);
	}

}
