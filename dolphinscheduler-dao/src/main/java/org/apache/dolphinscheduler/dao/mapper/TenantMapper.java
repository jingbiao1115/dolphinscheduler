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


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.dolphinscheduler.dao.entity.Tenant;
import org.apache.ibatis.annotations.Param;

/**
 * tenant mapper interface
 */
public interface TenantMapper extends BaseMapper<Tenant> {

	/**
	 * query tenant by id
	 *
	 * @param tenantId
	 *            tenantId
	 * @return tenant
	 */
	Tenant queryById(@Param("tenantId") int tenantId);

	/**
	 * delete by id
	 */
	int deleteById(int id);

	/**
	 * update
	 */
	int updateById(@Param("et") Tenant tenant);

	/**
	 * query tenant by code
	 *
	 * @param tenantCode
	 *            tenantCode
	 * @return tenant
	 */
	Tenant queryByTenantCode(@Param("tenantCode") String tenantCode);

	/**
	 * query tenants by queue id
	 *
	 * @param queueId
	 *            queue id
	 * @return tenant list
	 */
	List<Tenant> queryTenantListByQueueId(@Param("queueId") Integer queueId);
	/**
	 * tenant page
	 *
	 * @param page
	 *            page
	 * @param searchVal
	 *            searchVal
	 * @return tenant IPage
	 */
	IPage<Tenant> queryTenantPaging(IPage<Tenant> page, @Param("ids") List<Integer> ids,
			@Param("searchVal") String searchVal);

	/**
	 * check tenant exist
	 *
	 * @param tenantCode
	 *            tenantCode
	 * @return true if exist else return null
	 */
	Boolean existTenant(@Param("tenantCode") String tenantCode);

	/**
	 * queryTenantPagingByIds
	 * 
	 * @param page
	 * @param ids
	 * @param searchVal
	 * @return
	 */
	IPage<Tenant> queryTenantPagingByIds(Page<Tenant> page, @Param("ids") List<Integer> ids,
			@Param("searchVal") String searchVal);

	/**
	 * queryAll
	 * 
	 * @return
	 */
	List<Tenant> queryAll();
}
