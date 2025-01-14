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

package org.apache.dolphinscheduler.dao.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Date;
import org.apache.dolphinscheduler.common.enums.Flag;
import org.apache.dolphinscheduler.dao.BaseDaoTest;
import org.apache.dolphinscheduler.dao.entity.TaskGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskGroupMapperTest extends BaseDaoTest {

	@Autowired
	TaskGroupMapper taskGroupMapper;

	/**
	 * test insert
	 */
	public TaskGroup insertOne() {
		TaskGroup taskGroup = new TaskGroup();
		taskGroup.setName("task group");
		taskGroup.setId(1);
		taskGroup.setUserId(1);
		taskGroup.setStatus(Flag.YES);
		taskGroup.setGroupSize(10);
		taskGroup.setDescription("this is a task group");
		Date date = new Date(System.currentTimeMillis());
		taskGroup.setUpdateTime(date);
		taskGroup.setUpdateTime(date);

		taskGroupMapper.insert(taskGroup);
		return taskGroup;
	}

	/**
	 * test update
	 */
	@Test
	public void testUpdate() {
		TaskGroup taskGroup = insertOne();
		taskGroup.setGroupSize(100);
		taskGroup.setUpdateTime(new Date(System.currentTimeMillis()));
		int i = taskGroupMapper.updateById(taskGroup);
		Assertions.assertEquals(i, 1);
	}

	/**
	 * test CheckName
	 */
	@Test
	public void testCheckName() {
		TaskGroup taskGroup = insertOne();
		TaskGroup result = taskGroupMapper.queryByName(taskGroup.getUserId(), taskGroup.getName());
		Assertions.assertNotNull(result);
	}

	/**
	 * test queryTaskGroupPaging
	 */
	@Test
	public void testQueryTaskGroupPaging() {
		TaskGroup taskGroup = insertOne();
		Page<TaskGroup> page = new Page(1, 3);
		IPage<TaskGroup> taskGroupIPage = taskGroupMapper.queryTaskGroupPaging(page, taskGroup.getName(),
				taskGroup.getStatus().getCode());

		Assertions.assertEquals(taskGroupIPage.getTotal(), 1);
	}
}
