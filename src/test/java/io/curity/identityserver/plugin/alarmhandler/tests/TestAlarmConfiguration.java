/*
 *  Copyright 2021 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.identityserver.plugin.alarmhandler.tests;

import io.curity.identityserver.plugin.alarmhandler.EventsBridgeAlarmConfiguration;

/*
 * The alarm configuration for testing will point to the developer's AWS region
 */
public final class TestAlarmConfiguration implements EventsBridgeAlarmConfiguration {

    @Override
    public String id() {
        return null;
    }

    @Override
    public String getRegionName() {
        return "eu-west-2";
    }

    @Override
    public String getEventBusName() {
        return "curity.events";
    }

    @Override
    public String getDataSourceName() {
        return "curity.identityserver";
    }

    @Override
    public AWSAccessMethod getEventsBridgeAccessMethod() {
        return null;
    }
}
