package it.moro.smartWall;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Wall;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmartWall extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("SmartWall plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SmartWall plugin disabled!");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (!block.getType().name().endsWith("_WALL")) return;
        Player player = event.getPlayer();
        if(!player.isSneaking())return;
        float yaw = player.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360;
        float finalYaw = yaw;
        Bukkit.getScheduler().runTask(this, () -> {
            Block b = event.getBlock();
            BlockData bd = b.getBlockData();
            if (!(bd instanceof Wall wall)) return;
            wall.setHeight(BlockFace.NORTH, Wall.Height.NONE);
            wall.setHeight(BlockFace.SOUTH, Wall.Height.NONE);
            wall.setHeight(BlockFace.EAST, Wall.Height.NONE);
            wall.setHeight(BlockFace.WEST, Wall.Height.NONE);
            wall.setUp(false);
            if (finalYaw >= 45 && finalYaw < 135) {
                wall.setHeight(BlockFace.NORTH, Wall.Height.TALL);
                wall.setHeight(BlockFace.SOUTH, Wall.Height.TALL);
            } else if (finalYaw >= 135 && finalYaw < 225) {
                wall.setHeight(BlockFace.EAST, Wall.Height.TALL);
                wall.setHeight(BlockFace.WEST, Wall.Height.TALL);
            } else if (finalYaw >= 225 && finalYaw < 315) {
                wall.setHeight(BlockFace.NORTH, Wall.Height.TALL);
                wall.setHeight(BlockFace.SOUTH, Wall.Height.TALL);
            } else {
                wall.setHeight(BlockFace.EAST, Wall.Height.TALL);
                wall.setHeight(BlockFace.WEST, Wall.Height.TALL);
            }
            b.setBlockData(wall);
        });
    }

}
