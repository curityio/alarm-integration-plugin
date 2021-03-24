package io.curity.identityserver.plugin.alarmhandler.tests;

import java.time.Instant;
import io.curity.identityserver.plugin.alarmhandler.EventsBridgeAlarmHandler;
import io.curity.identityserver.plugin.alarmhandler.EventsBridgeManagedClient;
import org.junit.jupiter.api.Test;
import se.curity.identityserver.sdk.alarm.AlarmSeverity;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import static org.junit.jupiter.api.Assertions.fail;

public class EventsBridgeAlarmHandlerTests {

    /*
     * An integration test to trigger a test alarm and verify that the upload connects as expected
     */
    @Test
    public void EventsBridgeAlarmHandler_RaiseTestAlarm_SuccessfullyUploads() {

        EventsBridgeManagedClient client = new EventsBridgeManagedClient(new TestAlarmConfiguration());

        TestAlarm alarm = new TestAlarm(
                new TestAlarmIdentifier(),
                Instant.now(),
                AlarmSeverity.MAJOR,
                false,
                new TestAlarmDescription(),
                true);

        EventsBridgeAlarmHandler handler = new EventsBridgeAlarmHandler(client);
        handler.handle(alarm);

        client.close();
    }
}
