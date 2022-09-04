package com.neomechanical.neoutils;

import com.neomechanical.neoutils.server.IServerInfo;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class ServerMetrics implements IServerInfo {
    private final long startTimeMillis;
    private final String startTime;

    protected ServerMetrics() {
        startTimeMillis = System.currentTimeMillis();
        String pattern = "yyyy HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        startTime = simpleDateFormat.format(new Date());
    }

    public static long convertMilliseconds(long time, TimeDataType timeDataType) {
        switch (timeDataType) {
            case MILLISECONDS:
                return time;
            case SECONDS:
                return time / 1000;
            case MINUTES:
                return time / 60000;
            case HOURS:
                return time / 3600000;
            case DAYS:
                return time / 86400000;
        }
        return time;
    }

    @Override
    public long getUptime(TimeDataType timeDataType) {
        return convertMilliseconds(System.currentTimeMillis() - startTimeMillis, timeDataType);
    }

    @Override
    public String getUptimeInStandardForm() {
        Duration duration = Duration.ofMillis(startTimeMillis);
        long seconds = duration.getSeconds();
        int DD = (int) (seconds / (24 * 3600));
        int HH = (int) ((seconds % (24 * 3600)) / 3600);
        int MM = (int) ((seconds % (24 * 3600 * 3600)) / 60);
        int SS = (int) ((seconds % (24L * 3600L * 3600L * 60L)) / 60);
        return String.format("%s days %s hours %s minutes and %s seconds", DD, HH, MM, SS);
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public String getTime() {
        String pattern = "yyyy HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    public enum TimeDataType {
        MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS
    }
}
