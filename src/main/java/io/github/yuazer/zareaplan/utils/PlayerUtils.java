package io.github.yuazer.zareaplan.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class PlayerUtils {

    public static HashSet<String> getPlayerNamesBetweenLocations(Location loc1, Location loc2) {
        // 创建一个空的Set<String>对象，用于存储玩家名
        HashSet<String> playerNames = new HashSet<>();
        // 获取loc1和loc2的最小和最大的x, y, z坐标
        double minX = Math.min(loc1.getX(), loc2.getX());
        double minY = Math.min(loc1.getY(), loc2.getY());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxX = Math.max(loc1.getX(), loc2.getX());
        double maxY = Math.max(loc1.getY(), loc2.getY());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());
        // 遍历loc1和loc2所在的世界的所有玩家
        for (Player player : loc1.getWorld().getPlayers()) {
            // 获取玩家的位置
            Location playerLoc = player.getLocation();
            if (!playerLoc.getWorld().getName().equalsIgnoreCase(loc2.getWorld().getName())){
                continue;
            }
            // 判断玩家是否在loc1和loc2之间
            if (playerLoc.getX() >= minX && playerLoc.getX() <= maxX &&
                    playerLoc.getY() >= minY && playerLoc.getY() <= maxY &&
                    playerLoc.getZ() >= minZ && playerLoc.getZ() <= maxZ) {
                // 如果是，将玩家的名字添加到Set<String>对象中
                playerNames.add(player.getName());
            }
        }
        // 返回Set<String>对象
        return playerNames;
    }
}
