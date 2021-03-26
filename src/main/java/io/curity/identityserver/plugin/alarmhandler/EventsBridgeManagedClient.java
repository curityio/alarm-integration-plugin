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

import se.curity.identityserver.sdk.plugin.ManagedObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.EventBridgeClientBuilder;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

/*
 * An injectable object that is created once to ensure good performance
 * https://curity.io/docs/idsvr-java-plugin-sdk/latest/se/curity/identityserver/sdk/plugin/ManagedObject.html
 */
public class EventsBridgeManagedClient extends ManagedObject<EventsBridgeAlarmConfiguration> {

    private final EventsBridgeAlarmConfiguration _configuration;
    private EventBridgeClient _innerClient;

    public EventsBridgeManagedClient(EventsBridgeAlarmConfiguration configuration) {
        super(configuration);
        this._configuration = configuration;
    }

    public void initialize() {

        Region region = Region.of(this._configuration.getRegionName());
        EventBridgeClientBuilder builder = EventBridgeClient.builder()
                .region(region);

        if (this._configuration.getEventsBridgeAccessMethod() != null) {
            builder.credentialsProvider(new EventsBridgeCredentialsProvider(this._configuration).get());
        }

        this._innerClient = builder.build();
    }

    public PutEventsResponse notify(PutEventsRequest request) {
        return this._innerClient.putEvents(request);
    }

    public void close() {
        this._innerClient.close();
    }
}
