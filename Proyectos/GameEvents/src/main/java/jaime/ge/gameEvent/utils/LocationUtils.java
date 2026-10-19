package jaime.ge.gameEvent.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class LocationUtils {
    public static Location getRandomLocationBetween(Location[] locations){
        if(locations.length < 2) return null;
        return getRandomLocationBetween(locations[0], locations[1]);
    }

    public static Location getRandomLocationBetween(Location loc1, Location loc2) {
        World world = loc1.getWorld();

        double minX = Math.min(loc1.getX(), loc2.getX());
        double maxX = Math.max(loc1.getX(), loc2.getX());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        double randomX = minX + (Math.random() * (maxX - minX));
        double randomZ = minZ + (Math.random() * (maxZ - minZ));

        double y = loc1.getY() + 1; // Aparecerá un bloque más arriba

        return new Location(world, randomX, y, randomZ);
    }

    public static Location addOffsetToLocation(Location location, double x, double y, double z){
        return new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.z() + z);
    }

    public static boolean isInsideCorners(Location playerLoc, List<Location> locations){
        return isInsideCorners(playerLoc, locations.toArray(new Location[0]));
    }
    public static boolean isInsideCorners(Location playerLoc, Location[] locations) {
        double minX = Math.min(locations[0].getX(), locations[1].getX());
        double maxX = Math.max(locations[0].getX(), locations[1].getX());
        double minZ = Math.min(locations[0].getZ(), locations[1].getZ());
        double maxZ = Math.max(locations[0].getZ(), locations[1].getZ());

//        Bukkit.getConsoleSender().sendMessage("player x: " + playerLoc.getX() + " should be >= " + minX);
//        Bukkit.getConsoleSender().sendMessage("player x: " + playerLoc.getX() + " should be <= " + maxX);
//        Bukkit.getConsoleSender().sendMessage("player z: " + playerLoc.getZ() + " should be >= " + minZ);
//        Bukkit.getConsoleSender().sendMessage("player z: " + playerLoc.getZ() + " should be <= " + maxZ);

        int playerX = (int) Math.floor(playerLoc.getX());
        int playerZ = (int) Math.floor(playerLoc.getZ());

        return playerX>= minX && playerX <= maxX &&
                playerZ >= minZ && playerZ <= maxZ;
    }

    public static boolean isInsideArea(Location playerLoc, Location[] locations) {
        double minX = Math.min(locations[0].getX(), locations[1].getX());
        double maxX = Math.max(locations[0].getX(), locations[1].getX());
        double minY = Math.min(locations[0].getY(), locations[1].getY());
        double maxY = Math.max(locations[0].getY(), locations[1].getY());
        double minZ = Math.min(locations[0].getZ(), locations[1].getZ());
        double maxZ = Math.max(locations[0].getZ(), locations[1].getZ());

        int playerX = (int) Math.floor(playerLoc.getX());
        int playerY = (int) Math.floor(playerLoc.getY());
        int playerZ = (int) Math.floor(playerLoc.getZ());

        return playerX >= minX && playerX <= maxX &&
                playerY >= minY && playerY <= maxY &&
                playerZ >= minZ && playerZ <= maxZ;
    }


    public static void keepPlayerInArea(Player player, List<Location> locations) {
        keepPlayerInArea(player, locations.toArray(new Location[0]));
    }

    public static void keepPlayerInArea(Player player, Location[] locations) {
        Location loc1 = locations[0];
        Location loc2 = locations[1];
        Location loc = player.getLocation();
        double minX = Math.min(loc1.getX(), loc2.getX());
        double maxX = Math.max(loc1.getX(), loc2.getX());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        double x = loc.getX();
        double z = loc.getZ();

        if (x < minX) x = (int) Math.floor(minX);
        if (x > maxX) x = (int) Math.floor(maxX);

        if (z < minZ) z = (int) Math.floor(minZ);
        if (z > maxZ) z = (int) Math.floor(maxZ);

        player.teleport(new Location(loc.getWorld(), x, loc.getY(), z, loc.getYaw(), loc.getPitch()));
    }



    public static boolean isInsideCorners(Location loc, Location loc1, Location loc2) {
        double minX = Math.min(loc1.getX(), loc2.getX());
        double maxX = Math.max(loc1.getX(), loc2.getX());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());



        return loc.getX() >= minX && loc.getX() <= maxX &&
                loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }

    public static void iterateRegion(Location loc1, Location loc2, Consumer<Block> action) {
        World world = loc1.getWorld();

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    action.accept(block);
                }
            }
        }
    }

    public static String serialize(Location location){
        return location.getWorld().getName() + ";" +
                String.format("%.1f", location.getX()) + ";" +
                String.format("%.1f", location.getY())+ ";" +
                String.format("%.1f", location.getZ()) + ";";
    }

    public static Location deserialize(@Nullable String string){
        if(string == null) return null;

        String[] values = string.split(";");
        if(values.length != 4) return null;
        World world = Bukkit.getWorld(values[0]);
        if(world == null){
            Bukkit.getConsoleSender().sendMessage(colorize("&cGame event: Could not load world &l" + values[0] + "&c. You will get errors when minigames try to get to this world."));
            return null;
        }
        double x = Double.parseDouble(values[1]);
        double y = Double.parseDouble(values[2]);
        double z = Double.parseDouble(values[3]);
        return new Location(world, x, y, z);
    }

    public static Location getRandomLocationFromArray(Location[] locations){
        int randomNum = ThreadLocalRandom.current().nextInt(0, locations.length);
        return locations[randomNum];
    }

}
