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

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import se.curity.identityserver.sdk.alarm.Alarm;
import se.curity.identityserver.sdk.alarm.AlarmDescription;
import se.curity.identityserver.sdk.alarm.AlarmingResource;
import se.curity.identityserver.sdk.alarm.AlarmIdentifier;
import se.curity.identityserver.sdk.alarm.AlarmType;
import se.curity.identityserver.sdk.alarm.ResourceType;

/*
 * A utility class to use Jackson to format alarm data in a format that is useful for the cloud provider
 */
public final class JsonFormatter {

    private final ObjectMapper _mapper;

    public JsonFormatter() {
        this._mapper = new ObjectMapper();
    }

    /*
     * Write data to a structure that AWS will consume
     */
    public String alarmToJson(final Alarm alarm, final boolean verbose) {

        if (verbose) {
            return this.outputVerbose(alarm);
        } else {
            return this.outputConcise(alarm);
        }
    }

    private String outputConcise(final Alarm alarm) {

        ObjectNode root = this._mapper.createObjectNode();
        AlarmIdentifier identifier = alarm.getAlarmIdentifier();
        if (identifier != null) {

            String uniqueId = identifier.asUniqueString();
            this.putValueSafe(root, "id", uniqueId);

            AlarmType type = identifier.getAlarmType();
            if (type != null) {
                root.put("type", type.toString());
            }
        }

        root.put("updated", alarm.getUpdated().toString());
        this.putValueSafe(root, "severity", alarm.getSeverity().toString());
        root.put("isCleared", alarm.isCleared());
        root.put("isSelfTest", alarm.isSelfTest());

        AlarmDescription description = alarm.getDescription();
        if (description != null) {
            String value = description.getBriefDescription();
            this.putValueSafe(root, "description", value);
        }

        return root.toPrettyString();
    }

    private String outputVerbose(final Alarm alarm) {

        ObjectNode root = this._mapper.createObjectNode();

        AlarmIdentifier identifier = alarm.getAlarmIdentifier();
        if (identifier != null) {

            String uniqueId = identifier.asUniqueString();
            this.putValueSafe(root, "id", uniqueId);

            AlarmType type = identifier.getAlarmType();
            if (type != null) {
                root.put("type", type.toString());
            }

            AlarmingResource resource = identifier.getResource();
            if (resource != null) {
                root.set("resource", this.alarmingResourceToJson(resource));
            }
        }

        root.put("updated", alarm.getUpdated().toEpochMilli());
        root.put("severity", alarm.getSeverity().toString());
        root.put("isCleared", alarm.isCleared());
        root.put("isSelfTest", alarm.isSelfTest());

        AlarmDescription description = alarm.getDescription();
        if (description != null) {
            root.set("description", this.alarmDescriptionToJson(description));
        }

        return root.toPrettyString();
    }

    private ObjectNode alarmingResourceToJson(final AlarmingResource resource) {

        ObjectNode resourceNode = this._mapper.createObjectNode();
        String resourceId = resource.getResourceId();
        this.putValueSafe(resourceNode, "id", resourceId);
        return resourceNode;
    }

    private ObjectNode alarmDescriptionToJson(final AlarmDescription description) {

        ObjectNode descriptionNode = this._mapper.createObjectNode();

        String summary = description.getBriefDescription();
        this.putValueSafe(descriptionNode, "summary", summary);

        List<String> details = description.getDetailedDescription();
        if (details != null && details.size() > 0) {

            ArrayNode detailsNode = this._mapper.createArrayNode();
            details.forEach(detailsNode::add);
            descriptionNode.set("details", detailsNode);
        }

        String dashboardLink = description.getDashboardLink();
        this.putValueSafe(descriptionNode, "dashboardLink", dashboardLink);

        List<String> actions = description.getSuggestedActions();
        if (actions != null && actions.size() > 0) {

            ArrayNode actionsNode = this._mapper.createArrayNode();
            actions.forEach(actionsNode::add);
            descriptionNode.set("suggestedActions", actionsNode);
        }

        Map<ResourceType, AlarmDescription.ImpactedResourceGroup> resources = description.getImpactedResources();
        if (resources != null && resources.size() > 0) {

            ArrayNode resourcesNode = this._mapper.createArrayNode();
            resources.forEach((ResourceType t, AlarmDescription.ImpactedResourceGroup g) -> {
                ObjectNode impactedResourceNode = this.impactedResourceToJson(t, g);
                resourcesNode.add(impactedResourceNode);
            });
            descriptionNode.set("impactedResources", resourcesNode);
        }

        return descriptionNode;
    }

    private ObjectNode impactedResourceToJson(
            final ResourceType type,
            final AlarmDescription.ImpactedResourceGroup group) {

        ObjectNode resourceNode = this._mapper.createObjectNode();
        this.putValueSafe(resourceNode, "type", type.toString());
        this.putValueSafe(resourceNode, "title", group.getTitle());

        Set<String> resources = group.getImpactedResources();
        if (resources != null && resources.size() > 0) {

            ArrayNode itemsNode = this._mapper.createArrayNode();
            resources.forEach(itemsNode::add);
            resourceNode.set("items", itemsNode);
        }

        return resourceNode;
    }

    private void putValueSafe(final ObjectNode node, final String name, final String value) {

        if (value != null) {
            node.put(name, value);
        }
    }
}
