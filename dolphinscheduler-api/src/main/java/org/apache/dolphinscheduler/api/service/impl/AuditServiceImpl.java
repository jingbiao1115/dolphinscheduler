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

package org.apache.dolphinscheduler.api.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.dolphinscheduler.api.audit.AuditMessage;
import org.apache.dolphinscheduler.api.audit.AuditPublishService;
import org.apache.dolphinscheduler.api.dto.AuditDto;
import org.apache.dolphinscheduler.api.service.AuditService;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.common.enums.AuditOperationType;
import org.apache.dolphinscheduler.common.enums.AuditResourceType;
import org.apache.dolphinscheduler.dao.entity.AuditLog;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.dao.mapper.AuditLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl extends BaseServiceImpl implements AuditService {

	@Autowired
	private AuditLogMapper auditLogMapper;

	@Autowired
	private AuditPublishService publishService;

	/**
	 * add new audit log
	 *
	 * @param user
	 *            login user
	 * @param resourceType
	 *            resource type
	 * @param resourceId
	 *            resource id
	 * @param operation
	 *            operation type
	 */
	@Override
	public void addAudit(User user, AuditResourceType resourceType, Integer resourceId, AuditOperationType operation) {
		publishService.publish(new AuditMessage(user, new Date(), resourceType, operation, resourceId));
	}

	/**
	 * query audit log paging
	 *
	 * @param loginUser
	 *            login user
	 * @param resourceType
	 *            resource type
	 * @param operationType
	 *            operation type
	 * @param startDate
	 *            start time
	 * @param endDate
	 *            end time
	 * @param userName
	 *            query user name
	 * @param pageNo
	 *            page number
	 * @param pageSize
	 *            page size
	 * @return audit log string data
	 */
	@Override
	public PageInfo<AuditDto> queryLogListPaging(User loginUser, AuditResourceType resourceType,
			AuditOperationType operationType, String startDate, String endDate, String userName, Integer pageNo,
			Integer pageSize) {

		int[] resourceArray = null;
		if (resourceType != null) {
			resourceArray = new int[]{resourceType.getCode()};
		}

		int[] opsArray = null;
		if (operationType != null) {
			opsArray = new int[]{operationType.getCode()};
		}

		Date start = checkAndParseDateParameters(startDate);
		Date end = checkAndParseDateParameters(endDate);

		IPage<AuditLog> logIPage = auditLogMapper.queryAuditLog(new Page<>(pageNo, pageSize), resourceArray, opsArray,
				userName, start, end);
		List<AuditDto> auditDtos = logIPage.getRecords().stream().map(this::transformAuditLog)
				.collect(Collectors.toList());

		PageInfo<AuditDto> pageInfo = new PageInfo<>(pageNo, pageSize);
		int total = (int) auditDtos.size();
		pageInfo.setTotal(total);
		pageInfo.setTotalPage((int) Math.ceil((double) total / pageSize));
		pageInfo.setTotalList(auditDtos);
		return pageInfo;
	}

	/**
	 * transform AuditLog to AuditDto
	 *
	 * @param auditLog
	 *            audit log
	 * @return audit dto
	 */
	private AuditDto transformAuditLog(AuditLog auditLog) {
		AuditDto auditDto = new AuditDto();
		String resourceType = AuditResourceType.of(auditLog.getResourceType()).getMsg();
		auditDto.setResource(resourceType);
		auditDto.setOperation(AuditOperationType.of(auditLog.getOperation()).getMsg());
		auditDto.setUserName(auditLog.getUserName());
		auditDto.setResourceName(auditLogMapper.queryResourceNameByType(resourceType, auditLog.getResourceId()));
		auditDto.setTime(auditLog.getTime());
		return auditDto;
	}
}
