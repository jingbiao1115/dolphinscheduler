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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("t_ds_relation_namespace_user")
public class K8sNamespaceUser {

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * user id
	 */
	@TableField("user_id")
	private int userId;

	/**
	 * namespace id
	 */
	@TableField("namespace_id")
	private int namespaceId;

	/**
	 * k8s cluster
	 */
	@TableField(exist = false)
	private String k8s;

	/**
	 * namespace name
	 */
	@TableField(exist = false)
	private String namespaceName;

	/**
	 * user name
	 */
	@TableField(exist = false)
	private String userName;

	/**
	 * permission
	 */
	private int perm;

	@TableField("create_time")
	private Date createTime;

	@TableField("update_time")
	private Date updateTime;
}
