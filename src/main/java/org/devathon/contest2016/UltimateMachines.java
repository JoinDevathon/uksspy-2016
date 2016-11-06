package org.devathon.contest2016;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.machines.Packager;
import org.devathon.contest2016.machines.Unpacker;
import org.devathon.contest2016.storage.MachineLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UltimateMachines extends JavaPlugin {


    private int tick = 0;
    private CraftingRecipes craftingRecipes;
    public List<Machine> machinesTypes = new ArrayList<>();
    public List<Machine> machines =  new ArrayList<>();

    @Override
    public void onEnable() {
        craftingRecipes = new CraftingRecipes(this);
        craftingRecipes.setupRecipes();

        setupMachineTypes();

        new ListenerClass(this);
        new MachineLoader(this);

        machineTask();
    }

    private void setupMachineTypes(){
        machinesTypes.add(new Packager());
        machinesTypes.add(new Unpacker());
    }

    private void machineTask(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Machine machine : machines) machine.onTick(tick);
            tick++;
        },1,1);
    }

    //Gave up on trying to do this with reflection
    public Machine getMachine(String type, UUID uuid, Location loc, List<Block> blocks){
        String[] split = type.split("\\.");
        type = split[split.length - 1];

        if(type.equalsIgnoreCase("packager")){
            return new Packager(uuid, loc, blocks);
        }else if(type.equalsIgnoreCase("unpacker")){
            return new Unpacker(uuid, loc, blocks);
        }

        return null;
    }

    @Override
    public void onDisable() {
    }

}

