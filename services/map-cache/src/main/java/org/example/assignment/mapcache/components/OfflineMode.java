package org.example.assignment.mapcache.components;

public class OfflineMode {
    private static OfflineMode instance;
    private boolean enableOfflineMode;

    private OfflineMode() {
    }

    public static synchronized OfflineMode getInstance() {
        if (instance == null) {
            instance = new OfflineMode();
        }
        return instance;
    }

    public boolean isOfflineModeEnabled() {
        return enableOfflineMode;
    }

    public void setOfflineModeEnabled(boolean enableOfflineMode) {
        this.enableOfflineMode = enableOfflineMode;
    }
}
