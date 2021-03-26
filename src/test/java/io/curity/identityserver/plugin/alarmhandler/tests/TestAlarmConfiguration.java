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
