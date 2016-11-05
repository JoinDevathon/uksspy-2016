package org.devathon.contest2016;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.machines.Packager;
import org.devathon.contest2016.machines.Unpacker;
import org.devathon.contest2016.storage.MachineLoader;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onDisable() {
    }

}

