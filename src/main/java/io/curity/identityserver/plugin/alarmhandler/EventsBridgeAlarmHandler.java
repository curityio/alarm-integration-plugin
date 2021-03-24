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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.alarm.Alarm;
import se.curity.identityserver.sdk.alarm.AlarmHandler;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResultEntry;

/*
 * A handler that sends alarm data to the AWS Events Bridge using the AWS Java SDK
 */
public final class EventsBridgeAlarmHandler implements AlarmHandler {

    private final EventsBridgeManagedClient _eventsBridgeClient;;
    private final Logger _logger;

    public EventsBridgeAlarmHandler(final EventsBridgeManagedClient client) {
        this._eventsBridgeClient = client;
        this._logger = LoggerFactory.getLogger(EventsBridgeAlarmHandler.class);
    }

    public void handle(final Alarm alarm) {

        this._logger.info("AWS alarm event triggered");
        String json = new JsonFormatter().alarmToJson(alarm, false);
        this._logger.debug(json);

        PutEventsResponse result = this._eventsBridgeClient.raiseAlarmEvent(json);

        for (PutEventsResultEntry resultEntry : result.entries()) {
            if (resultEntry.eventId() != null) {
                this._logger.info("AWS alarm event sent successfully: " + resultEntry.eventId());
            } else {
                this._logger.info("AWS alarm event failed to send: " + resultEntry.errorCode());
            }
        }
    }
}
