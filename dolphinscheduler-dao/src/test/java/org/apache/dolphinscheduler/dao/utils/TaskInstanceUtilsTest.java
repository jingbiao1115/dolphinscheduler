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

package org.apache.dolphinscheduler.dao.utils;


import java.util.Date;
import java.util.HashMap;
import org.apache.dolphinscheduler.common.utils.JSONUtils;
import org.apache.dolphinscheduler.dao.entity.TaskInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskInstanceUtilsTest {

	@Test
	void copyTaskInstance() {
		TaskInstance source = new TaskInstance();
		source.setId(1);
		source.setName("source");
		source.setSubmitTime(new Date());
		source.setTaskParams(JSONUtils.toJsonString(new HashMap<>()));
		TaskInstance target = new TaskInstance();
		TaskInstanceUtils.copyTaskInstance(source, target);
		Assertions.assertEquals(target.getId(), source.getId());
		Assertions.assertEquals(target.getName(), source.getName());
	}
}
