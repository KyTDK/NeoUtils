package com.neomechanical.neoutils.server;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

@UtilityClass
public class ServerUtils {

    private static final Field CURRENT_TICK_FIELD;

    static {
        Field tmpCurrentTickField;
        try {
            tmpCurrentTickField = Bukkit.getScheduler().getClass().getDeclaredField("currentTick");
            tmpCurrentTickField.setAccessible(true);
        } catch (final Exception ignored) {
            tmpCurrentTickField = null;
        }
        CURRENT_TICK_FIELD = tmpCurrentTickField;
    }

    /**
     * Returns the server's current life phase - when you call this in onEnable or onDisable, and it returns RUNNING, it means the server is reloading.
     *
     * @return Server's life phase
     */
    public ServerLifePhase getLifePhase() {
        //try {
        int currentTicket = getCurrentTick();
        if (currentTicket == -1) {
            return ServerLifePhase.STARTUP;
        } else if (currentTicket == -2) {
            return ServerLifePhase.UNKNOWN;
        }
        return ServerLifePhase.RUNNING;
    }

    /**
     * Returns the current tick count, or -1 if the server is still starting up, or -2 if we couldn't get the current tick count.
     *
     * @return current tick count, or -1 if the server is still starting up, or -2 if we couldn't get the current tick count.
     */
    public int getCurrentTick() {
        if (CURRENT_TICK_FIELD == null) return -2;
        try {
            return CURRENT_TICK_FIELD.getInt(Bukkit.getScheduler());
        } catch (final IllegalAccessException e) {
            return -2;
        }
    }

    public enum ServerLifePhase {
        STARTUP, RUNNING, UNKNOWN
    }
}
