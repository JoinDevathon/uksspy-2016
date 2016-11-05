package org.devathon.contest2016.storage;

import org.bukkit.event.Listener;
import org.devathon.contest2016.UltimateMachines;

import java.io.File;

public class MachineLoader implements Listener {

    private UltimateMachines plugin;

    public MachineLoader(UltimateMachines plugin){
        this.plugin = plugin;
        createFolder();
    }

    private void createFolder(){
        if(!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdir()){
            plugin.getLogger().severe("Unable to create plugin folder. Plugin will not behave correctly");
        }

        File machineFolder = new File(plugin.getDataFolder() + File.separator + "machines");
        if(!machineFolder.exists() && !machineFolder.mkdir()){
            plugin.getLogger().severe("Unable to create machines folder. Plugin will not behave correctly");
        }
    }
}
