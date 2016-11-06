package org.devathon.contest2016;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.machines.*;
import org.devathon.contest2016.storage.MachineLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UltimateMachines extends JavaPlugin {


    private int tick = 0;
    private CraftingRecipes craftingRecipes;
    private MachineLoader machineLoader;

    public List<Machine> machinesTypes = new ArrayList<>();
    public List<Machine> machines =  new ArrayList<>();

    @Override
    public void onEnable() {
        craftingRecipes = new CraftingRecipes(this);
        craftingRecipes.setupRecipes();

        setupMachineTypes();

        new ListenerClass(this);
        machineLoader = new MachineLoader(this);

        machineTask();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("ultimatemachines")){
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("get")){
                    if(sender instanceof Player){
                        Player player = (Player) sender;
                        if(player.hasPermission("ultimatemachines.get")){
                            if(args.length == 2){

                                String machineStr = args[1];
                                for(Machine machine : machinesTypes){
                                    if(machine.getName().equalsIgnoreCase(machineStr)){

                                        ItemStack itemStack = new ItemStack(Material.IRON_INGOT, 1);
                                        ItemMeta im = itemStack.getItemMeta();
                                        im.setDisplayName(ChatColor.BLUE + machine.getName());
                                        itemStack.setItemMeta(im);

                                        player.getInventory().addItem(itemStack);
                                        player.sendMessage(ChatColor.GREEN + "Gave you a " + machine.getName());
                                        return true;
                                    }
                                }
                                player.sendMessage(ChatColor.RED + "There is no machine with the name " + machineStr);

                            }else{
                                player.sendMessage(ChatColor.RED + "Usage: /" + label + " get <machine_name>");
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "You do not have the permission to use this command");
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED + "This command must be executed by a player.");
                    }
                }
            }
        }
        return false;
    }

    private void setupMachineTypes(){
        machinesTypes.add(new Packager());
        machinesTypes.add(new Unpacker());
        machinesTypes.add(new Drainer());
        machinesTypes.add(new Incubator());
        machinesTypes.add(new LavaGenerator());
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
        }else if(type.equalsIgnoreCase("drainer")){
            return new Drainer(uuid, loc, blocks);
        }else if(type.equalsIgnoreCase("incubator")){
            return new Incubator(uuid, loc, blocks);
        }else if(type.equalsIgnoreCase("lavagenerator")){
            return new LavaGenerator(uuid, loc, blocks);
        }

        return null;
    }

    @Override
    public void onDisable() {
        machineLoader.saveMachines();
    }

}

