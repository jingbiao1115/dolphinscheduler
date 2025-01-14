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
import org.apache.dolphinscheduler.dao.entity.K8sNamespaceUser;
import org.apache.ibatis.annotations.Param;

/**
 * namespace user mapper interface
 */
public interface K8sNamespaceUserMapper extends BaseMapper<K8sNamespaceUser> {

	/**
	 * delete namespace user relation
	 *
	 * @param namespaceId
	 *            namespaceId
	 * @param userId
	 *            userId
	 * @return delete result
	 */
	int deleteNamespaceRelation(@Param("namespaceId") int namespaceId, @Param("userId") int userId);

	/**
	 * query namespace relation
	 *
	 * @param namespaceId
	 *            namespaceId
	 * @param userId
	 *            userId
	 * @return namespace user relation
	 */
	K8sNamespaceUser queryNamespaceRelation(@Param("namespaceId") int namespaceId, @Param("userId") int userId);
}
