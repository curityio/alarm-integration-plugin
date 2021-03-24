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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.alarm.Alarm;
import se.curity.identityserver.sdk.alarm.AlarmHandler;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.EventBridgeException;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResultEntry;

/*
 * A handler that sends alarm data to the AWS Events Bridge using the AWS Java SDK
 */
public final class EventsBridgeAlarmHandler implements AlarmHandler {

    private final EventsBridgeAlarmConfiguration _configuration;
    private final Logger _logger;

    public EventsBridgeAlarmHandler(final EventsBridgeAlarmConfiguration configuration) {
        this._configuration = configuration;
        this._logger = LoggerFactory.getLogger(EventsBridgeAlarmHandler.class);
    }

    public void handle(final Alarm alarm) {

        this._logger.info("AWS alarm event being handled");
        String json = new JsonFormatter().alarmToJson(alarm, false);
        this._logger.debug(json);

        Region region = Region.of(this._configuration.getRegionName());
        try (EventBridgeClient client = EventBridgeClient.builder()
                .region(region)
                .build()) {

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

            PutEventsResponse result = client.putEvents(eventsRequest);

            for (PutEventsResultEntry resultEntry : result.entries()) {
                if (resultEntry.eventId() != null) {
                    this._logger.info("AWS alarm event sent successfully: " + resultEntry.eventId());
                } else {
                    this._logger.info("AWS alarm event failed to send: " + resultEntry.errorCode());
                }
            }

        } catch (EventBridgeException e) {

            this._logger.info("AWS alarm event connectivity error: " + e.awsErrorDetails().errorMessage());
        }
    }
}
