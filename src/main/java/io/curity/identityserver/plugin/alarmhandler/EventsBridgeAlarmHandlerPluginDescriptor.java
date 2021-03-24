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

import se.curity.identityserver.sdk.alarm.AlarmHandler;
import se.curity.identityserver.sdk.plugin.descriptor.AlarmHandlerPluginDescriptor;

/*
 * Describe our custom alarm handler plugin
 */
public final class EventsBridgeAlarmHandlerPluginDescriptor
        implements AlarmHandlerPluginDescriptor<EventsBridgeAlarmConfiguration> {

    /*
     * Return an alarm instance
     */
    @Override
    public Class<? extends AlarmHandler> getAlarmHandler() {
        return EventsBridgeAlarmHandler.class;
    }

    /*
     * Get a unique string to describe the plugin
     */
    @Override
    public String getPluginImplementationType() {
        return "AWS-events-bridge-notifier";
    }

    /*
     * Indicate the configuration used
     */
    @Override
    public Class<? extends EventsBridgeAlarmConfiguration> getConfigurationType() {
        return EventsBridgeAlarmConfiguration.class;
    }
}
