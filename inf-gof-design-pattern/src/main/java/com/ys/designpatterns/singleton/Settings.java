package com.ys.designpatterns.singleton;

import java.io.Serializable;

/**
 * @author : ysk
 */

public class Settings implements Serializable {

    private Settings() {}

    private static class SettingsHolder {
        private static final Settings SETTINGS = new Settings();
    }

    public static Settings getInstance() {
        return SettingsHolder.SETTINGS;
    }

    protected Object readResolve() {
        return getInstance();
    }
}
