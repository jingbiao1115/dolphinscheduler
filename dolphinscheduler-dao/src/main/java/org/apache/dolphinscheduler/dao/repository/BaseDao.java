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

package org.apache.dolphinscheduler.dao.repository;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

public abstract class BaseDao<ENTITY, MYBATIS_MAPPER extends BaseMapper<ENTITY>> implements IDao<ENTITY> {

	protected MYBATIS_MAPPER mybatisMapper;

	public BaseDao(@NonNull MYBATIS_MAPPER mybatisMapper) {
		this.mybatisMapper = mybatisMapper;
	}

	@Override
	public ENTITY queryById(@NonNull Serializable id) {
		return mybatisMapper.selectById(id);
	}

	@Override
	public Optional<ENTITY> queryOptionalById(@NonNull Serializable id) {
		return Optional.ofNullable(queryById(id));
	}

	@Override
	public List<ENTITY> queryByIds(Collection<? extends Serializable> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		return mybatisMapper.selectBatchIds(ids);
	}

	@Override
	public List<ENTITY> queryByCondition(ENTITY queryCondition) {
		if (queryCondition == null) {
			throw new IllegalArgumentException("queryCondition can not be null");
		}
		return mybatisMapper.selectList(new QueryWrapper<>(queryCondition));
	}

	@Override
	public int insert(@NonNull ENTITY model) {
		return mybatisMapper.insert(model);
	}

	@Override
	public void insertBatch(Collection<ENTITY> models) {
		if (CollectionUtils.isEmpty(models)) {
			return;
		}
		for (ENTITY model : models) {
			insert(model);
		}
	}

	@Override
	public boolean updateById(@NonNull ENTITY model) {
		return mybatisMapper.updateById(model) > 0;
	}

	@Override
	public boolean deleteById(@NonNull Serializable id) {
		return mybatisMapper.deleteById(id) > 0;
	}

	@Override
	public boolean deleteByIds(Collection<? extends Serializable> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return true;
		}
		return mybatisMapper.deleteBatchIds(ids) > 0;
	}

	@Override
	public boolean deleteByCondition(ENTITY queryCondition) {
		if (queryCondition == null) {
			throw new IllegalArgumentException("queryCondition can not be null");
		}
		return mybatisMapper.delete(new QueryWrapper<>(queryCondition)) > 0;
	}

}
