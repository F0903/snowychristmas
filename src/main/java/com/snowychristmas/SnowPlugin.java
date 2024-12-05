package com.snowychristmas;

import org.bukkit.plugin.java.JavaPlugin;

public class SnowPlugin extends JavaPlugin {
    SnowSystem snowSystem;

    @Override
    public void onEnable() {
        snowSystem = new SnowSystem(this);
        snowSystem.start();
    }

    @Override
    public void onDisable() {
        snowSystem.stop();
    }
}
