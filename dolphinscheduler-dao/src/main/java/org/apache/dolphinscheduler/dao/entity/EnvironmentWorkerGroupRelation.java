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

package org.apache.dolphinscheduler.dao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("t_ds_environment_worker_group_relation")
public class EnvironmentWorkerGroupRelation {

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * environment code
	 */
	private Long environmentCode;

	/**
	 * worker group id
	 */
	private String workerGroup;

	/**
	 * operator user id
	 */
	private Integer operator;

	private Date createTime;

	private Date updateTime;
}
