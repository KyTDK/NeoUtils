package com.neomechanical.neoutils.server;

import com.neomechanical.neoutils.ServerMetrics;

public interface IServerInfo {
    long getUptime(ServerMetrics.TimeDataType timeDataType);

    String getUptimeInStandardForm();

    String getStartTime();

    String getTime();

}
