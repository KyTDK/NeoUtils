package com.neomechanical.neoutils;

import junit.framework.TestCase;

public class ServerMetricsTest extends TestCase {

    public void testMillisecondsToDay() {
        assertEquals(1, ServerMetrics.convertMilliseconds(86400000, ServerMetrics.TimeDataType.DAYS));
    }

    public void testMillisecondsToHour() {
        assertEquals(1, ServerMetrics.convertMilliseconds(3600000, ServerMetrics.TimeDataType.HOURS));
    }

    public void testMillisecondsToMinute() {
        assertEquals(1, ServerMetrics.convertMilliseconds(60000, ServerMetrics.TimeDataType.MINUTES));
    }

    public void testMillisecondsToSecond() {
        assertEquals(1, ServerMetrics.convertMilliseconds(1000, ServerMetrics.TimeDataType.SECONDS));
    }

    public void testMillisecondsToMilliseconds() {
        assertEquals(1, ServerMetrics.convertMilliseconds(1, ServerMetrics.TimeDataType.MILLISECONDS));
    }
}