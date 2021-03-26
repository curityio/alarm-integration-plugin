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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import se.curity.identityserver.sdk.alarm.Alarm;
import se.curity.identityserver.sdk.alarm.AlarmDescription;
import se.curity.identityserver.sdk.alarm.AlarmSeverity;
import se.curity.identityserver.sdk.alarm.AlarmingResource;
import se.curity.identityserver.sdk.alarm.AlarmIdentifier;
import se.curity.identityserver.sdk.alarm.AlarmType;

/*
 * A utility class to format alarm data in whatever format is useful for the cloud provider
 */
public final class JsonFormatter {

    private final ObjectMapper _mapper;

    public JsonFormatter() {
        this._mapper = new ObjectMapper();
    }

    public String getConciseAlarmPayload(final Alarm alarm) {

        ObjectNode root = this._mapper.createObjectNode();
        AlarmIdentifier identifier = alarm.getAlarmIdentifier();
        if (identifier != null) {

            AlarmingResource resource = identifier.getResource();
            if (resource != null) {
                root.put("resource", resource.getResourceId());
            }

            String qualifier = identifier.getQualifier();
            if (qualifier != null) {
                root.put("qualifier", qualifier);
            }

            AlarmSeverity severity = alarm.getSeverity();
            if (severity != null) {
                root.put("severity", severity.name());
            }

            root.put("updated", alarm.getUpdated().toString());

            if (resource != null) {
                root.put("service", resource.getNodeId());
            }

            AlarmType type = identifier.getAlarmType();
            if (type != null) {
                root.put("type", type.name());
            }

            AlarmDescription description = alarm.getDescription();
            if (description != null) {
                root.put("text", description.getBriefDescription());
            }
        }

        AlarmDescription description = alarm.getDescription();
        if (description != null) {
            root.put("text", description.getBriefDescription());
        }

        return root.toPrettyString();
    }
}
