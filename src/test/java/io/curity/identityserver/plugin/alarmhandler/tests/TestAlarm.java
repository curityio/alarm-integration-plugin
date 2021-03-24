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

package io.curity.identityserver.plugin.alarmhandler.tests;

import java.time.Instant;
import se.curity.identityserver.sdk.alarm.Alarm;
import se.curity.identityserver.sdk.alarm.AlarmDescription;
import se.curity.identityserver.sdk.alarm.AlarmIdentifier;
import se.curity.identityserver.sdk.alarm.AlarmSeverity;

/*
 * An alarm instance for testing, to provide some alarm data to send
 */
public final class TestAlarm implements Alarm {

    private final AlarmIdentifier _alarmIdentifier;
    private final Instant _updated;
    private final AlarmSeverity _severity;
    private final boolean _cleared;
    private final AlarmDescription _description;
    private final boolean _isSelfTest;

    public TestAlarm(final AlarmIdentifier alarmIdentifier,
                     final Instant updated,
                     final AlarmSeverity severity,
                     final boolean cleared,
                     final AlarmDescription description,
                     final boolean isSelfTest) {

        this._alarmIdentifier = alarmIdentifier;
        this._updated = updated;
        this._severity = severity;
        this._cleared = cleared;
        this._description = description;
        this._isSelfTest = isSelfTest;
    }

    @Override
    public AlarmIdentifier getAlarmIdentifier() {
        return this._alarmIdentifier;
    }

    @Override
    public Instant getUpdated() {
        return this._updated;
    }

    @Override
    public AlarmSeverity getSeverity() {
        return this._severity;
    }

    @Override
    public boolean isCleared() {
        return this._cleared;
    }

    @Override
    public boolean isSelfTest() {
        return this._isSelfTest;
    }

    @Override
    public AlarmDescription getDescription() {
        return this._description;
    }
}
