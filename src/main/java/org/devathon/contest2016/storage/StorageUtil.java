package org.devathon.contest2016.storage;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class StorageUtil {

    /**
     * Does not preserve World
     * @param loc
     * @return
     */
    public static String sterilizeLocation(Location loc){
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    public static Location desterilizeLocation(String loc, World world){
        String[] split = loc.split(",");
        return new Location(world, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

}
