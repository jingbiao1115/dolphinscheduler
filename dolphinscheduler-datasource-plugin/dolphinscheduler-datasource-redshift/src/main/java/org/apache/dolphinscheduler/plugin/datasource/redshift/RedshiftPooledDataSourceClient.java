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

package org.apache.dolphinscheduler.plugin.datasource.redshift;


import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dolphinscheduler.plugin.datasource.api.client.BasePooledDataSourceClient;
import org.apache.dolphinscheduler.plugin.datasource.redshift.param.RedshiftAuthMode;
import org.apache.dolphinscheduler.plugin.datasource.redshift.param.RedshiftConnectionParam;
import org.apache.dolphinscheduler.plugin.datasource.redshift.param.RedshiftDataSourceProcessor;
import org.apache.dolphinscheduler.spi.datasource.BaseConnectionParam;
import org.apache.dolphinscheduler.spi.enums.DbType;

@Slf4j
public class RedshiftPooledDataSourceClient extends BasePooledDataSourceClient {

	public RedshiftPooledDataSourceClient(BaseConnectionParam baseConnectionParam, DbType dbType) {
		super(baseConnectionParam, dbType);
	}

	@Override
	public Connection getConnection() throws SQLException {
		RedshiftConnectionParam connectionParam = (RedshiftConnectionParam) this.baseConnectionParam;
		if (connectionParam.getMode().equals(RedshiftAuthMode.PASSWORD)) {
			return super.getConnection();
		}
		return RedshiftDataSourceProcessor.getConnectionByIAM(connectionParam);
	}

}
