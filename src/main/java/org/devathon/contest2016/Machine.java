package org.devathon.contest2016;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Machine {


    private String name;

    public Machine(){

    }

    public Machine(Location loc){

    }

    public abstract void createMachine(Player p);

    public abstract void deleteMachine();

    public abstract void onTick(int tick);

    public abstract boolean isMachine(Block block);

    public String getName(){
        return name;
    }

    public boolean hasRoom(Inventory inv, ItemStack itemStack){
        if(itemStack.getAmount() > 64) return false;

        int room = 0;

        for(int slot = 0; slot < inv.getSize(); slot++){
            ItemStack item = inv.getItem(slot);
            if(item == null || item.getType() == Material.AIR) return true;
            if(item.getType() == itemStack.getType()){
                if(item.getMaxStackSize() - item.getAmount() >= 0) room += item.getMaxStackSize() - item.getAmount();
            }
        }
        return room >= itemStack.getAmount();
    }
}
