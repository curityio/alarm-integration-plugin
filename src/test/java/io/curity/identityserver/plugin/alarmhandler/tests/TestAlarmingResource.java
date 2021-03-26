package io.curity.identityserver.plugin.alarmhandler.tests;

import se.curity.identityserver.sdk.alarm.AlarmingResource;
import se.curity.identityserver.sdk.alarm.ResourceType;

public class TestAlarmingResource implements AlarmingResource {
    @Override
    public ResourceType getResourceType() {
        return null;
    }

    @Override
    public String getResourceId() {
        return "custom-claims-api-client";
    }

    @Override
    public String getNodeId() {
        return "1THAkvyM";
    }
}
