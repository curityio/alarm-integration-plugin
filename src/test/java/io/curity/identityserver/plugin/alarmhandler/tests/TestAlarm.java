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

        _alarmIdentifier = alarmIdentifier;
        _updated = updated;
        _severity = severity;
        _cleared = cleared;
        _description = description;
        _isSelfTest = isSelfTest;
    }

    @Override
    public AlarmIdentifier getAlarmIdentifier() {
        return _alarmIdentifier;
    }

    @Override
    public Instant getUpdated() {
        return _updated;
    }

    @Override
    public AlarmSeverity getSeverity() {
        return _severity;
    }

    @Override
    public boolean isCleared() {
        return _cleared;
    }

    @Override
    public boolean isSelfTest() {
        return _isSelfTest;
    }

    @Override
    public AlarmDescription getDescription() {
        return _description;
    }
}
