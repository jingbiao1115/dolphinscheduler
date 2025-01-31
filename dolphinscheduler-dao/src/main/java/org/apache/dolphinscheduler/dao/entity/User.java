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
import org.apache.dolphinscheduler.common.enums.UserType;

@Data
@TableName("t_ds_user")
public class User {

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	private String userName;

	private String userPassword;

	private String email;

	private String phone;

	private UserType userType;

	private int tenantId;

	private int state;

	@TableField(exist = false)
	private String tenantCode;

	@TableField(exist = false)
	private String queueName;

	@TableField(exist = false)
	private String alertGroup;

	private String queue;

	private String timeZone;

	private Date createTime;

	private Date updateTime;

}
