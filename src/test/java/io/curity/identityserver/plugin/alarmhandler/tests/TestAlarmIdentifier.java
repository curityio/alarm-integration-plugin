package io.curity.identityserver.plugin.alarmhandler.tests;

import se.curity.identityserver.sdk.alarm.AlarmIdentifier;
import se.curity.identityserver.sdk.alarm.AlarmType;
import se.curity.identityserver.sdk.alarm.AlarmingResource;

/*
 * An alarm identifier for testing, based on a real HTTP client failure
 */
public final class TestAlarmIdentifier implements AlarmIdentifier {

    @Override
    public AlarmingResource getResource() {
        return new TestAlarmingResource();
    }

    @Override
    public AlarmType getAlarmType() {
        return AlarmType.EXTERNAL_SERVICE_FAILED_CONNECTION;
    }

    @Override
    public String getQualifier() {
        return "https://api.mycompany.com/myapi";
    }

    @Override
    public String asUniqueString() {
        return "/base:environments/environment/services/runtime-service[id='1THAkvyM']/../../../../base:facilities/http/client[id='custom-claims-api-client']";
    }
}
