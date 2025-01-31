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

package org.apache.dolphinscheduler.api.dto.taskInstance;


import lombok.Data;
import org.apache.dolphinscheduler.api.utils.Result;

/**
 * task instance success response
 */
@Data
public class TaskInstanceRemoveCacheResponse extends Result {

	private String cacheKey;

	public TaskInstanceRemoveCacheResponse(Result result) {
		super();
		this.setCode(result.getCode());
		this.setMsg(result.getMsg());
	}

	public TaskInstanceRemoveCacheResponse(Result result, String cacheKey) {
		super();
		this.setCode(result.getCode());
		this.setMsg(result.getMsg());
		this.cacheKey = cacheKey;
	}
}
