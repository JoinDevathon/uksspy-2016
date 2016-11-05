package org.devathon.contest2016;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ListenerClass implements Listener{

    private UltimateMachines plugin;

    public ListenerClass(UltimateMachines plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getItem() == null || event.getItem().getType() != Material.IRON_INGOT || !event.getItem().hasItemMeta()) return;

            ItemStack item = event.getItem();
            plugin.machinesTypes.stream().filter(machine -> ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(machine.getName())).forEach(machine -> {
                try {

                    Machine newMachine = machine.getClass().getConstructor(UUID.class, Location.class).newInstance(UUID.randomUUID(), event.getClickedBlock().getLocation().add(0, 1, 0));
                    newMachine.createMachine(event.getPlayer());
                    plugin.machines.add(newMachine);

                } catch (InstantiationException
                        | IllegalAccessException
                        | InvocationTargetException
                        | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        for(Machine machine : plugin.machines){
            if(machine.isMachine(event.getBlock())){
                machine.deleteMachine();
                plugin.machines.remove(machine);
                break;
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event){
        for(Machine machine : plugin.machines){
            if(machine.isMachine(event.getBlock())){
                machine.deleteMachine();
                plugin.machines.remove(machine);
                break;
            }
        }
    }
}