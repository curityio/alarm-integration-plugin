package io.curity.identityserver.plugin.alarmhandler;

import java.time.Instant;
import se.curity.identityserver.sdk.plugin.ManagedObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

/*
 * An injectable object that is created once to ensure good performance
 * https://curity.io/docs/idsvr-java-plugin-sdk/latest/se/curity/identityserver/sdk/plugin/ManagedObject.html
 */
public class EventsBridgeManagedClient extends ManagedObject<EventsBridgeAlarmConfiguration> {

    EventsBridgeAlarmConfiguration _configuration;
    EventBridgeClient _innerClient;

    public EventsBridgeManagedClient(EventsBridgeAlarmConfiguration configuration) {
        super(configuration);
        this._configuration = configuration;

        Region region = Region.of(this._configuration.getRegionName());
        this._innerClient = EventBridgeClient.builder()
                .region(region)
                .build();
    }

    public PutEventsResponse raiseAlarmEvent(String json) {

        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                .eventBusName(this._configuration.getEventBusName())
                .source(this._configuration.getDataSourceName())
                .detailType("alarm")
                .detail(json)
                .time(Instant.now())
                .build();

        PutEventsRequest eventsRequest = PutEventsRequest.builder()
                .entries(entry)
                .build();

        return this._innerClient.putEvents(eventsRequest);
    }

    public void close() {
        this._innerClient.close();
    }
}
