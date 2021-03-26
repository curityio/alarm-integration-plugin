package io.curity.identityserver.plugin.alarmhandler.tests;

import java.time.Instant;
import io.curity.identityserver.plugin.alarmhandler.EventsBridgeAlarmHandler;
import io.curity.identityserver.plugin.alarmhandler.EventsBridgeManagedClient;
import org.junit.jupiter.api.Test;
import se.curity.identityserver.sdk.alarm.AlarmSeverity;

public class EventsBridgeAlarmHandlerTests {

    /*
     * An integration test to trigger a test alarm and verify that the upload connects as expected
     */
    @Test
    public void EventsBridgeAlarmHandler_RaiseTestAlarm_SuccessfullyUploads() {

        TestAlarmConfiguration configuration = new TestAlarmConfiguration();
        try (EventsBridgeManagedClient client = new EventsBridgeManagedClient(configuration)) {

            client.initialize();
            TestAlarm alarm = new TestAlarm(
                    new TestAlarmIdentifier(),
                    Instant.now(),
                    AlarmSeverity.MAJOR,
                    false,
                    new TestAlarmDescription(),
                    true);

            EventsBridgeAlarmHandler handler = new EventsBridgeAlarmHandler(configuration, client);
            handler.handle(alarm);
        }
    }
}
