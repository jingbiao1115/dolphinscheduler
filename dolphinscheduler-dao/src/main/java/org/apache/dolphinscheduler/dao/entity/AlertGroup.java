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
import java.util.Objects;
import lombok.Data;

@Data
@TableName("t_ds_alertgroup")
public class AlertGroup {

	/**
	 * primary key
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * group_name
	 */
	@TableField(value = "group_name")
	private String groupName;

	@TableField(value = "alert_instance_ids")
	private String alertInstanceIds;

	/**
	 * description
	 */
	@TableField(value = "description")
	private String description;
	/**
	 * create_time
	 */
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * update_time
	 */
	@TableField(value = "update_time")
	private Date updateTime;

	/**
	 * create_user_id
	 */
	@TableField(value = "create_user_id")
	private int createUserId;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AlertGroup that = (AlertGroup) o;

		if (!Objects.equals(id, that.id)) {
			return false;
		}
		if (createUserId != that.createUserId) {
			return false;
		}
		if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) {
			return false;
		}
		if (alertInstanceIds != null
				? !alertInstanceIds.equals(that.alertInstanceIds)
				: that.alertInstanceIds != null) {
			return false;
		}
		if (description != null ? !description.equals(that.description) : that.description != null) {
			return false;
		}
		return !(createTime != null ? !createTime.equals(that.createTime) : that.createTime != null)
				&& !(updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + createUserId;
		result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
		result = 31 * result + (alertInstanceIds != null ? alertInstanceIds.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
		result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
		return result;
	}
}
