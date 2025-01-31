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

package org.apache.dolphinscheduler.plugin.datasource.redshift.param;


import lombok.Getter;
import lombok.Setter;
import org.apache.dolphinscheduler.plugin.datasource.api.datasource.BaseDataSourceParamDTO;
import org.apache.dolphinscheduler.spi.enums.DbType;

@Getter
@Setter
public class RedshiftDataSourceParamDTO extends BaseDataSourceParamDTO {

	protected RedshiftAuthMode mode;
	protected String dbUser;

	@Override
	public DbType getType() {
		return DbType.REDSHIFT;
	}

	@Override
	public String toString() {
		return "RedshiftDataSourceParamDTO{" + "name='" + name + '\'' + ", note='" + note + '\'' + ", host='" + host
				+ '\'' + ", port=" + port + ", database='" + database + '\'' + ", userName='" + userName + '\''
				+ ", password='" + password + '\'' + ", other='" + other + '\'' + ", dbUser='" + dbUser + '\''
				+ ", mode='" + mode + '\'' + '}';
	}
}
