package com.snowychristmas;

import java.util.Map;

import org.bukkit.Bukkit;

public class Reflection {
    Map<String, Class<?>> classes;

    public Class<?> getNMSClass(String name) throws ClassNotFoundException {
        var classObj = classes.get(name);

        if (classObj == null) {
            classObj = Class.forName("net.minecraft.server"
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
            classes.put(name, classObj);
            return classObj;
        }

        return classObj;
    }
}
