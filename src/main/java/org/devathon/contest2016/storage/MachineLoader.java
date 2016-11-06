package org.devathon.contest2016.storage;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.devathon.contest2016.Machine;
import org.devathon.contest2016.UltimateMachines;

import javax.crypto.Mac;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
    Full disclaimer: I would really prefer to use a SQL database for this but since we can't use
    external libs I opted for this mess of flat files.
 */
public class MachineLoader implements Listener {

    private UltimateMachines plugin;
    private File machineFolder;

    public MachineLoader(UltimateMachines plugin){
        this.plugin = plugin;
        createFolder();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Bukkit.getServer().getWorlds().forEach(this::loadMachinesInWorld);
    }

    private void createFolder(){
        if(!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdir()){
            plugin.getLogger().severe("Unable to create plugin folder. Plugin will not behave correctly");
        }

        machineFolder = new File(plugin.getDataFolder() + File.separator + "machines");
        if(!machineFolder.exists() && !machineFolder.mkdir()){
            plugin.getLogger().severe("Unable to create machines folder. Plugin will not behave correctly");
        }
    }

    /**
     * Called on startup
     * @param world
     */
    public void loadMachinesInWorld(World world){
        for(Chunk chunk : world.getLoadedChunks()){
            File chunkFolder = new File(machineFolder + File.separator + world.getName() + File.separator + chunk.getX() + " " + chunk.getZ());
            if(chunkFolder.exists()){
                loadMachinesInChunk(world, chunkFolder);
            }else{
                chunkFolder.mkdirs();
            }
        }
    }

    /**
     * Called on shutdown
     */
    public void saveMachines(){
        for(World world : plugin.getServer().getWorlds()){
            for(Chunk chunk : world.getLoadedChunks()){
                saveMachinesInChunkSync(chunk);
            }
        }
    }

    private void loadMachinesInChunk(World world, File chunkFolder){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (File file : chunkFolder.listFiles()) {
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

                String fileName = file.getName().split("\\.")[0];

                final String type = conf.getString("type");
                int loc_x = conf.getInt("location.x");
                int loc_y = conf.getInt("location.y");
                int loc_z = conf.getInt("location.z");
                List<String> machineBlocks = conf.getStringList("machineBlocks");
                List<Block> blocks = machineBlocks.stream().map(blockStr -> StorageUtil.desterilizeLocation(blockStr, world).getBlock()).collect(Collectors.toList());

                file.delete();

                Bukkit.getScheduler().runTask(plugin, () ->{

                    Machine machine = plugin.getMachine(type, UUID.fromString(fileName), new Location(world, loc_x, loc_y, loc_z), blocks);
                    if(machine != null) plugin.machines.add(machine);
                });
            }
        });
    }

    private void saveMachinesInChunk(Chunk chunk){
        File chunkFolder = new File(machineFolder + File.separator + chunk.getWorld().getName() + File.separator + chunk.getX() + " " + chunk.getZ());

        List<Machine> machinesInChunk = plugin.machines.stream().filter(machine -> machine.getLoc().getChunk().equals(chunk)).collect(Collectors.toList());
        if(machinesInChunk.size() > 0) System.out.println("Saved " + machinesInChunk.size() + " machines in chunk " + chunk);
        plugin.machines.removeAll(machinesInChunk);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for(Machine machine : machinesInChunk){
                File file = new File(chunkFolder + File.separator + machine.getUuid() + ".yml");
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

                conf.set("type", machine.getClass().getName());

                conf.set("location.x", machine.getLoc().getBlockX());
                conf.set("location.y", machine.getLoc().getBlockY());
                conf.set("location.z", machine.getLoc().getBlockZ());

                List<String> machineBlocks = machine.getBlocks().stream().map(b -> StorageUtil.sterilizeLocation(b.getLocation())).collect(Collectors.toList());
                conf.set("machineBlocks", machineBlocks);

                try {
                    conf.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sync because you can't run tasks on disable
     * @param chunk
     */
    private void saveMachinesInChunkSync(Chunk chunk){
        File chunkFolder = new File(machineFolder + File.separator + chunk.getWorld().getName() + File.separator + chunk.getX() + " " + chunk.getZ());

        List<Machine> machinesInChunk = plugin.machines.stream().filter(machine -> machine.getLoc().getChunk().equals(chunk)).collect(Collectors.toList());
        if(machinesInChunk.size() > 0) System.out.println("Saved " + machinesInChunk.size() + " machines in chunk " + chunk);
        plugin.machines.removeAll(machinesInChunk);

        for(Machine machine : machinesInChunk){
            File file = new File(chunkFolder + File.separator + machine.getUuid() + ".yml");
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

            conf.set("type", machine.getClass().getName());

            conf.set("location.x", machine.getLoc().getBlockX());
            conf.set("location.y", machine.getLoc().getBlockY());
            conf.set("location.z", machine.getLoc().getBlockZ());

            List<String> machineBlocks = machine.getBlocks().stream().map(b -> StorageUtil.sterilizeLocation(b.getLocation())).collect(Collectors.toList());
            conf.set("machineBlocks", machineBlocks);

            try {
                conf.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /*
        Listeners
     */
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        File chunkFolder = new File(machineFolder + File.separator + event.getWorld().getName() + File.separator + event.getChunk().getX() + " " + event.getChunk().getZ());
        if(chunkFolder.exists()){
            loadMachinesInChunk(event.getWorld(), chunkFolder);
        }else{
            Bukkit.getScheduler().runTaskAsynchronously(plugin, chunkFolder::mkdirs);
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        saveMachinesInChunk(event.getChunk());
    }
}
