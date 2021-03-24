package io.curity.identityserver.plugin.alarmhandler.tests;

import java.time.Instant;

import io.curity.identityserver.plugin.alarmhandler.EventsBridgeAlarmHandler;
import org.junit.jupiter.api.Test;
import se.curity.identityserver.sdk.alarm.AlarmSeverity;
import static org.junit.jupiter.api.Assertions.fail;

public class EventsBridgeAlarmHandlerTests {

    /*
     * An integration test to trigger a test alarm and verify that the upload connects as expected
     */
    @Test
    public void EventsBridgeAlarmHandler_RaiseTestAlarm_SuccessfullyUploads() {

        try {

            TestAlarm alarm = new TestAlarm(
                    new TestAlarmIdentifier(),
                    Instant.now(),
                    AlarmSeverity.MAJOR,
                    false,
                    new TestAlarmDescription(),
                    true);

            EventsBridgeAlarmHandler handler = new EventsBridgeAlarmHandler(new TestAlarmConfiguration());
            handler.handle(alarm);

        } catch (Throwable ex) {

            fail("Problem encountered sending alarm data to AWS: " + ex.getMessage());
        }
    }
}
