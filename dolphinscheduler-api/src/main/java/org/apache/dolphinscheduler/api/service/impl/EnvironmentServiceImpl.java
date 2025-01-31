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

import static org.apache.dolphinscheduler.api.constants.ApiFuncIdentificationConstant.ENVIRONMENT_CREATE;
import static org.apache.dolphinscheduler.api.constants.ApiFuncIdentificationConstant.ENVIRONMENT_DELETE;
import static org.apache.dolphinscheduler.api.constants.ApiFuncIdentificationConstant.ENVIRONMENT_UPDATE;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dolphinscheduler.api.dto.EnvironmentDto;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.exceptions.ServiceException;
import org.apache.dolphinscheduler.api.service.EnvironmentService;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.common.constants.Constants;
import org.apache.dolphinscheduler.common.enums.AuthorizationType;
import org.apache.dolphinscheduler.common.enums.UserType;
import org.apache.dolphinscheduler.common.utils.CodeGenerateUtils;
import org.apache.dolphinscheduler.common.utils.CodeGenerateUtils.CodeGenerateException;
import org.apache.dolphinscheduler.common.utils.JSONUtils;
import org.apache.dolphinscheduler.dao.entity.Environment;
import org.apache.dolphinscheduler.dao.entity.EnvironmentWorkerGroupRelation;
import org.apache.dolphinscheduler.dao.entity.TaskDefinition;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.dao.mapper.EnvironmentMapper;
import org.apache.dolphinscheduler.dao.mapper.EnvironmentWorkerGroupRelationMapper;
import org.apache.dolphinscheduler.dao.mapper.TaskDefinitionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * task definition service impl
 */
@Service
@Slf4j
public class EnvironmentServiceImpl extends BaseServiceImpl implements EnvironmentService {

	@Autowired
	private EnvironmentMapper environmentMapper;

	@Autowired
	private EnvironmentWorkerGroupRelationMapper relationMapper;

	@Autowired
	private TaskDefinitionMapper taskDefinitionMapper;

	/**
	 * create environment
	 *
	 * @param loginUser
	 *            login user
	 * @param name
	 *            environment name
	 * @param config
	 *            environment config
	 * @param desc
	 *            environment desc
	 * @param workerGroups
	 *            worker groups
	 */
	@Override
	@Transactional
	public Long createEnvironment(User loginUser, String name, String config, String desc, String workerGroups) {
		if (!canOperatorPermissions(loginUser, null, AuthorizationType.ENVIRONMENT, ENVIRONMENT_CREATE)) {
			throw new ServiceException(Status.USER_NO_OPERATION_PERM);
		}
		if (checkDescriptionLength(desc)) {
			throw new ServiceException(Status.DESCRIPTION_TOO_LONG_ERROR);
		}
		checkParams(name, config, workerGroups);

		Environment environment = environmentMapper.queryByEnvironmentName(name);
		if (environment != null) {
			throw new ServiceException(Status.ENVIRONMENT_NAME_EXISTS, name);
		}

		Environment env = new Environment();
		env.setName(name);
		env.setConfig(config);
		env.setDescription(desc);
		env.setOperator(loginUser.getId());
		env.setCreateTime(new Date());
		env.setUpdateTime(new Date());
		long code = 0L;
		try {
			code = CodeGenerateUtils.getInstance().genCode();
			env.setCode(code);
		} catch (CodeGenerateException e) {
			log.error("Generate environment code error.", e);
		}
		if (code == 0L) {
			throw new ServiceException(Status.INTERNAL_SERVER_ERROR_ARGS, "Error generating environment code");
		}

		if (environmentMapper.insert(env) > 0) {
			if (!StringUtils.isEmpty(workerGroups)) {
				List<String> workerGroupList = JSONUtils.parseObject(workerGroups, new TypeReference<List<String>>() {
				});
				if (CollectionUtils.isNotEmpty(workerGroupList)) {
					workerGroupList.stream().forEach(workerGroup -> {
						if (!StringUtils.isEmpty(workerGroup)) {
							EnvironmentWorkerGroupRelation relation = new EnvironmentWorkerGroupRelation();
							relation.setEnvironmentCode(env.getCode());
							relation.setWorkerGroup(workerGroup);
							relation.setOperator(loginUser.getId());
							relation.setCreateTime(new Date());
							relation.setUpdateTime(new Date());
							relationMapper.insert(relation);
							log.info(
									"Environment-WorkerGroup relation create complete, environmentName:{}, workerGroup:{}.",
									env.getName(), relation.getWorkerGroup());
						}
					});
				}
			}
			return env.getCode();
		}
		throw new ServiceException(Status.CREATE_ENVIRONMENT_ERROR);
	}

	/**
	 * query environment paging
	 *
	 * @param pageNo
	 *            page number
	 * @param searchVal
	 *            search value
	 * @param pageSize
	 *            page size
	 * @return environment list page
	 */
	@Override
	public Result queryEnvironmentListPaging(User loginUser, Integer pageNo, Integer pageSize, String searchVal) {
		Result<Object> result = new Result();

		Page<Environment> page = new Page<>(pageNo, pageSize);
		PageInfo<EnvironmentDto> pageInfo = new PageInfo<>(pageNo, pageSize);
		IPage<Environment> environmentIPage;
		if (loginUser.getUserType().equals(UserType.ADMIN_USER)) {
			environmentIPage = environmentMapper.queryEnvironmentListPaging(page, searchVal);
		} else {
			Set<Integer> ids = resourcePermissionCheckService
					.userOwnedResourceIdsAcquisition(AuthorizationType.ENVIRONMENT, loginUser.getId(), log);
			if (ids.isEmpty()) {
				result.setData(pageInfo);
				putMsg(result, Status.SUCCESS);
				return result;
			}
			environmentIPage = environmentMapper.queryEnvironmentListPagingByIds(page, new ArrayList<>(ids), searchVal);
		}

		int total = (int) environmentIPage.getTotal();
		// pageInfo.setTotal((int) environmentIPage.getTotal());
		pageInfo.setTotal(total);

		if (CollectionUtils.isNotEmpty(environmentIPage.getRecords())) {
			Map<Long, List<String>> relationMap = relationMapper.selectList(null).stream()
					.collect(Collectors.groupingBy(EnvironmentWorkerGroupRelation::getEnvironmentCode,
							Collectors.mapping(EnvironmentWorkerGroupRelation::getWorkerGroup, Collectors.toList())));

			List<EnvironmentDto> dtoList = environmentIPage.getRecords().stream().map(environment -> {
				EnvironmentDto dto = new EnvironmentDto();
				BeanUtils.copyProperties(environment, dto);
				List<String> workerGroups = relationMap.getOrDefault(environment.getCode(), new ArrayList<String>());
				dto.setWorkerGroups(workerGroups);
				return dto;
			}).collect(Collectors.toList());

			pageInfo.setTotalPage((int) Math.ceil((double) total / pageSize));

			pageInfo.setTotalList(dtoList);
		} else {
			pageInfo.setTotalList(new ArrayList<>());
		}

		result.setData(pageInfo);
		putMsg(result, Status.SUCCESS);
		return result;
	}

	/**
	 * query all environment
	 *
	 * @param loginUser
	 * @return all environment list
	 */
	@Override
	public Map<String, Object> queryAllEnvironmentList(User loginUser) {
		Map<String, Object> result = new HashMap<>();
		Set<Integer> ids = resourcePermissionCheckService.userOwnedResourceIdsAcquisition(AuthorizationType.ENVIRONMENT,
				loginUser.getId(), log);
		if (ids.isEmpty()) {
			result.put(Constants.DATA_LIST, Collections.emptyList());
			putMsg(result, Status.SUCCESS);
			return result;
		}
		List<Environment> environmentList = environmentMapper.selectBatchIds(ids);
		if (CollectionUtils.isNotEmpty(environmentList)) {
			Map<Long, List<String>> relationMap = relationMapper.selectList(null).stream()
					.collect(Collectors.groupingBy(EnvironmentWorkerGroupRelation::getEnvironmentCode,
							Collectors.mapping(EnvironmentWorkerGroupRelation::getWorkerGroup, Collectors.toList())));

			List<EnvironmentDto> dtoList = environmentList.stream().map(environment -> {
				EnvironmentDto dto = new EnvironmentDto();
				BeanUtils.copyProperties(environment, dto);
				List<String> workerGroups = relationMap.getOrDefault(environment.getCode(), new ArrayList<String>());
				dto.setWorkerGroups(workerGroups);
				return dto;
			}).collect(Collectors.toList());
			result.put(Constants.DATA_LIST, dtoList);
		} else {
			result.put(Constants.DATA_LIST, new ArrayList<>());
		}

		putMsg(result, Status.SUCCESS);
		return result;
	}

	/**
	 * query environment
	 *
	 * @param code
	 *            environment code
	 */
	@Override
	public Map<String, Object> queryEnvironmentByCode(Long code) {
		Map<String, Object> result = new HashMap<>();

		Environment env = environmentMapper.queryByEnvironmentCode(code);

		if (env == null) {
			putMsg(result, Status.QUERY_ENVIRONMENT_BY_CODE_ERROR, code);
		} else {
			List<String> workerGroups = relationMapper.queryByEnvironmentCode(env.getCode()).stream()
					.map(item -> item.getWorkerGroup()).collect(Collectors.toList());

			EnvironmentDto dto = new EnvironmentDto();
			BeanUtils.copyProperties(env, dto);
			dto.setWorkerGroups(workerGroups);
			result.put(Constants.DATA_LIST, dto);
			putMsg(result, Status.SUCCESS);
		}
		return result;
	}

	/**
	 * query environment
	 *
	 * @param name
	 *            environment name
	 */
	@Override
	public Map<String, Object> queryEnvironmentByName(String name) {
		Map<String, Object> result = new HashMap<>();

		Environment env = environmentMapper.queryByEnvironmentName(name);
		if (env == null) {
			putMsg(result, Status.QUERY_ENVIRONMENT_BY_NAME_ERROR, name);
		} else {
			List<String> workerGroups = relationMapper.queryByEnvironmentCode(env.getCode()).stream()
					.map(item -> item.getWorkerGroup()).collect(Collectors.toList());

			EnvironmentDto dto = new EnvironmentDto();
			BeanUtils.copyProperties(env, dto);
			dto.setWorkerGroups(workerGroups);
			result.put(Constants.DATA_LIST, dto);
			putMsg(result, Status.SUCCESS);
		}
		return result;
	}

	/**
	 * delete environment
	 *
	 * @param loginUser
	 *            login user
	 * @param code
	 *            environment code
	 */
	@Transactional
	@Override
	public Map<String, Object> deleteEnvironmentByCode(User loginUser, Long code) {
		Map<String, Object> result = new HashMap<>();
		if (!canOperatorPermissions(loginUser, null, AuthorizationType.ENVIRONMENT, ENVIRONMENT_DELETE)) {
			putMsg(result, Status.USER_NO_OPERATION_PERM);
			return result;
		}

		Long relatedTaskNumber = taskDefinitionMapper
				.selectCount(new QueryWrapper<TaskDefinition>().lambda().eq(TaskDefinition::getEnvironmentCode, code));

		if (relatedTaskNumber > 0) {
			log.warn("Delete environment failed because {} tasks is using it, environmentCode:{}.", relatedTaskNumber,
					code);
			putMsg(result, Status.DELETE_ENVIRONMENT_RELATED_TASK_EXISTS);
			return result;
		}

		int delete = environmentMapper.deleteByCode(code);
		if (delete > 0) {
			relationMapper.delete(new QueryWrapper<EnvironmentWorkerGroupRelation>().lambda()
					.eq(EnvironmentWorkerGroupRelation::getEnvironmentCode, code));
			log.info("Environment and relations delete complete, environmentCode:{}.", code);
			putMsg(result, Status.SUCCESS);
		} else {
			log.error("Environment delete error, environmentCode:{}.", code);
			putMsg(result, Status.DELETE_ENVIRONMENT_ERROR);
		}
		return result;
	}

	/**
	 * update environment
	 *
	 * @param loginUser
	 *            login user
	 * @param code
	 *            environment code
	 * @param name
	 *            environment name
	 * @param config
	 *            environment config
	 * @param desc
	 *            environment desc
	 * @param workerGroups
	 *            worker groups
	 */
	@Transactional
	@Override
	public Environment updateEnvironmentByCode(User loginUser, Long code, String name, String config, String desc,
			String workerGroups) {
		if (!canOperatorPermissions(loginUser, null, AuthorizationType.ENVIRONMENT, ENVIRONMENT_UPDATE)) {
			throw new ServiceException(Status.USER_NO_OPERATION_PERM);
		}

		checkParams(name, config, workerGroups);
		if (checkDescriptionLength(desc)) {
			throw new ServiceException(Status.DESCRIPTION_TOO_LONG_ERROR);
		}

		Environment environment = environmentMapper.queryByEnvironmentName(name);
		if (environment != null && !environment.getCode().equals(code)) {
			throw new ServiceException(Status.ENVIRONMENT_NAME_EXISTS, name);
		}

		Set<String> workerGroupSet;
		if (!StringUtils.isEmpty(workerGroups)) {
			workerGroupSet = JSONUtils.parseObject(workerGroups, new TypeReference<Set<String>>() {
			});
		} else {
			workerGroupSet = new TreeSet<>();
		}

		Set<String> existWorkerGroupSet = relationMapper.queryByEnvironmentCode(code).stream()
				.map(EnvironmentWorkerGroupRelation::getWorkerGroup).collect(Collectors.toSet());

		Set<String> deleteWorkerGroupSet = SetUtils.difference(existWorkerGroupSet, workerGroupSet).toSet();
		Set<String> addWorkerGroupSet = SetUtils.difference(workerGroupSet, existWorkerGroupSet).toSet();

		// verify whether the relation of this environment and worker groups can be
		// adjusted
		checkUsedEnvironmentWorkerGroupRelation(deleteWorkerGroupSet, name, code);

		Environment env = new Environment();
		env.setCode(code);
		env.setName(name);
		env.setConfig(config);
		env.setDescription(desc);
		env.setOperator(loginUser.getId());
		env.setUpdateTime(new Date());

		int update = environmentMapper.update(env,
				new UpdateWrapper<Environment>().lambda().eq(Environment::getCode, code));
		if (update <= 0) {
			throw new ServiceException(Status.UPDATE_ENVIRONMENT_ERROR, name);
		}
		deleteWorkerGroupSet.forEach(key -> {
			if (StringUtils.isNotEmpty(key)) {
				relationMapper.delete(new QueryWrapper<EnvironmentWorkerGroupRelation>().lambda()
						.eq(EnvironmentWorkerGroupRelation::getEnvironmentCode, code)
						.eq(EnvironmentWorkerGroupRelation::getWorkerGroup, key));
			}
		});
		addWorkerGroupSet.forEach(key -> {
			if (StringUtils.isNotEmpty(key)) {
				EnvironmentWorkerGroupRelation relation = new EnvironmentWorkerGroupRelation();
				relation.setEnvironmentCode(code);
				relation.setWorkerGroup(key);
				relation.setUpdateTime(new Date());
				relation.setCreateTime(new Date());
				relation.setOperator(loginUser.getId());
				relationMapper.insert(relation);
			}
		});
		return env;
	}

	/**
	 * verify environment name
	 *
	 * @param environmentName
	 *            environment name
	 * @return true if the environment name not exists, otherwise return false
	 */
	@Override
	public Map<String, Object> verifyEnvironment(String environmentName) {
		Map<String, Object> result = new HashMap<>();

		if (StringUtils.isEmpty(environmentName)) {
			log.warn("parameter environment name is empty.");
			putMsg(result, Status.ENVIRONMENT_NAME_IS_NULL);
			return result;
		}

		Environment environment = environmentMapper.queryByEnvironmentName(environmentName);
		if (environment != null) {
			log.warn("Environment with the same name already exist, name:{}.", environment.getName());
			putMsg(result, Status.ENVIRONMENT_NAME_EXISTS, environmentName);
			return result;
		}

		result.put(Constants.STATUS, Status.SUCCESS);
		return result;
	}

	private void checkUsedEnvironmentWorkerGroupRelation(Set<String> deleteKeySet, String environmentName,
			Long environmentCode) {
		for (String workerGroup : deleteKeySet) {
			List<TaskDefinition> taskDefinitionList = taskDefinitionMapper.selectList(
					new QueryWrapper<TaskDefinition>().lambda().eq(TaskDefinition::getEnvironmentCode, environmentCode)
							.eq(TaskDefinition::getWorkerGroup, workerGroup));

			if (Objects.nonNull(taskDefinitionList) && taskDefinitionList.size() != 0) {
				Set<String> collect = taskDefinitionList.stream().map(TaskDefinition::getName)
						.collect(Collectors.toSet());
				throw new ServiceException(Status.UPDATE_ENVIRONMENT_WORKER_GROUP_RELATION_ERROR, workerGroup,
						environmentName, collect);
			}
		}
	}

	protected void checkParams(String name, String config, String workerGroups) {
		if (StringUtils.isEmpty(name)) {
			throw new ServiceException(Status.ENVIRONMENT_NAME_IS_NULL);
		}
		if (StringUtils.isEmpty(config)) {
			throw new ServiceException(Status.ENVIRONMENT_CONFIG_IS_NULL);
		}
		if (!StringUtils.isEmpty(workerGroups)) {
			List<String> workerGroupList = JSONUtils.parseObject(workerGroups, new TypeReference<List<String>>() {
			});
			if (Objects.isNull(workerGroupList)) {
				throw new ServiceException(Status.ENVIRONMENT_WORKER_GROUPS_IS_INVALID);
			}
		}
	}

}
