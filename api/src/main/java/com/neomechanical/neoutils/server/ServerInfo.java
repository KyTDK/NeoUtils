package com.neomechanical.neoutils.server;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.ServerMetrics;

public class ServerInfo implements IServerInfo {
    private final ServerMetrics serverMetrics;

    public ServerInfo() {
        serverMetrics = NeoUtils.getInstance().getServerMetrics();
    }

    public long getUptime(ServerMetrics.TimeDataType timeDataType) {
        return serverMetrics.getUptime(timeDataType);
    }

    @Override
    public String getUptimeInStandardForm() {
        return serverMetrics.getUptimeInStandardForm();
    }

    public String getStartTime() {
        return serverMetrics.getStartTime();
    }

    public String getTime() {
        return serverMetrics.getTime();
    }
}
