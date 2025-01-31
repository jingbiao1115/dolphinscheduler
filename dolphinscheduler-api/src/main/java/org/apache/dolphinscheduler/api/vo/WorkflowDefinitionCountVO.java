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

package org.apache.dolphinscheduler.api.vo;


import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dolphinscheduler.dao.model.WorkflowDefinitionCountDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDefinitionCountVO {

	private int count;

	private List<WorkflowDefinitionCountDto> userList;

	public static WorkflowDefinitionCountVO empty() {
		return new WorkflowDefinitionCountVO(0, Collections.emptyList());
	}

	public static WorkflowDefinitionCountVO of(List<WorkflowDefinitionCountDto> workflowDefinitionCounts) {
		if (CollectionUtils.isEmpty(workflowDefinitionCounts)) {
			return empty();
		}
		WorkflowDefinitionCountVO workflowDefinitionCountVo = new WorkflowDefinitionCountVO();
		workflowDefinitionCountVo.setUserList(workflowDefinitionCounts);
		workflowDefinitionCountVo
				.setCount(workflowDefinitionCounts.stream().mapToInt(WorkflowDefinitionCountDto::getCount).sum());
		return workflowDefinitionCountVo;
	}

}
