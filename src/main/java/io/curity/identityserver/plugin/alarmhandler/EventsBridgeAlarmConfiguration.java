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

package io.curity.identityserver.plugin.alarmhandler;

import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.config.annotation.DefaultString;
import se.curity.identityserver.sdk.config.annotation.Description;

/*
 * These properties will be configured in the Curity Identity Server under Alarm Handlers
 */
public interface EventsBridgeAlarmConfiguration extends Configuration {

    @Description("The AWS region name")
    @DefaultString("us-east-1")
    String getRegionName();

    @Description("The AWS event bus that will receive the alarm data")
    @DefaultString("curity.events")
    String getEventBusName();

    @Description("The AWS event rule's pattern will use this value")
    @DefaultString("curity.identityserver")
    String getDataSourceName();
}
