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

package org.apache.dolphinscheduler.plugin.storage.s3;


import com.google.auto.service.AutoService;
import org.apache.dolphinscheduler.plugin.storage.api.StorageOperate;
import org.apache.dolphinscheduler.plugin.storage.api.StorageOperateFactory;
import org.apache.dolphinscheduler.plugin.storage.api.StorageType;

@AutoService(StorageOperateFactory.class)
public class S3StorageOperatorFactory implements StorageOperateFactory {

	@Override
	public StorageOperate createStorageOperate() {
		S3StorageOperator s3StorageOperator = new S3StorageOperator();
		s3StorageOperator.init();
		return s3StorageOperator;
	}

	@Override
	public StorageType getStorageOperate() {
		return StorageType.S3;
	}
}
