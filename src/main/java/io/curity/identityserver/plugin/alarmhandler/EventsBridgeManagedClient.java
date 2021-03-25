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

import java.time.Instant;
import se.curity.identityserver.sdk.plugin.ManagedObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.EventBridgeClientBuilder;
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
    }

    /*
     * Create the client once and handle AWS credential variations
     */
    public void initialize() {

        Region region = Region.of(this._configuration.getRegionName());
        EventBridgeClientBuilder builder = EventBridgeClient.builder()
                .region(region);

        EventsBridgeAlarmConfiguration.AWSAccessMethod accessMethod = this._configuration.getEventsBridgeAccessMethod();
        if (accessMethod != null) {
            EventsBridgeCredentialsProvider.get(this._configuration.getEventsBridgeAccessMethod());
            builder.credentialsProvider(
                    EventsBridgeCredentialsProvider.get(this._configuration.getEventsBridgeAccessMethod()));
        }

        this._innerClient = builder.build();
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
