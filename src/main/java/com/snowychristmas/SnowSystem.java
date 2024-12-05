package com.snowychristmas;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtVisitor;

public class SnowSystem {
    private final JavaPlugin plugin;

    private final int duration = 20 * 60 * 60; // Lasts an hour, then updates again.

    private BukkitRunnable stormWeatherRunnable;

    private boolean _debugWrote;

    String bytesToString(byte[] arr) {
        var sb = new StringBuffer();
        for (byte b : arr) {
            sb.append(b);
        }
        return String.format("Byte Array len = %d, value = [%s]", arr.length, sb.toString());
    }

    void interpretChunkDataBuffer(byte[] data) {
        var logger = this.plugin.getLogger();
        var chunkStream = new DataInputStream(new ByteArrayInputStream(data));
        try {
            var nonAirBlocks = chunkStream.readShort();
            var bitsPerEntry = chunkStream.readByte();
            var blockStateIdAir = chunkStream.readByte();
            var lengthOfLongArray = chunkStream.readByte();
            var biomeBitsPerEntry = chunkStream.readByte();
            var biomePaletteLength = chunkStream.readByte();
            var biomePaletteElem1 = chunkStream.readByte();
            var biomePaletteElem2 = chunkStream.readByte();
            var biomeIndexedDataNum = chunkStream.readByte();

            logger.info(String.valueOf(nonAirBlocks));
            logger.info(String.valueOf(bitsPerEntry));
            logger.info(String.valueOf(bitsPerEntry));
            logger.info(String.valueOf(blockStateIdAir));
            logger.info(String.valueOf(lengthOfLongArray));
            logger.info(String.valueOf(biomeBitsPerEntry));
            logger.info(String.valueOf(biomePaletteLength));
            logger.info(String.valueOf(biomePaletteElem1));
            logger.info(String.valueOf(biomePaletteElem2));
            logger.info(String.valueOf(biomeIndexedDataNum));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public SnowSystem(JavaPlugin plugin) {
        this.plugin = plugin;

        // Setup a task that runs every n ticks.
        stormWeatherRunnable = new BukkitRunnable() {
            public void run() {
                var worlds = Bukkit.getWorlds();
                for (var world : worlds) {
                    world.setStorm(true);
                    world.setWeatherDuration(duration);
                }
            }
        };

        this.plugin.getLogger().info("Setting up packet listener...");

        var logger = this.plugin.getLogger();

        var pm = ProtocolLibrary.getProtocolManager();
        pm.addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Server.MAP_CHUNK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                var packet = event.getPacket();
                var a = packet.getIntegers();

                for (int i = 0; i < a.size(); i++) {
                    // 3 is snowy plains
                    a.write(i, 3);
                }

                // TODO: make work

                if (!_debugWrote) {
                    _debugWrote = true;
                }
            }
        });

        this.plugin.getLogger().info("Setup done.");
    }

    public void start() {
        stormWeatherRunnable.runTaskTimer(plugin, 0, duration);
    }

    public void stop() {
        stormWeatherRunnable.cancel();
    }
}
