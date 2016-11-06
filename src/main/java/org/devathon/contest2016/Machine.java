package org.devathon.contest2016;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Machine {


    private String name;
    private UUID uuid;
    private Location loc;
    private List<Block> machineBlocks;

    public Machine(){}

    public Machine(String name){
        this.name = name;
        machineBlocks = new ArrayList<>();
    }

    public Machine(UUID uuid, Location loc){
        this.uuid = uuid;
        this.loc = loc;
        machineBlocks = new ArrayList<>();
    }

    public Machine(UUID uuid, Location loc, List<Block> blocks){
        this.uuid = uuid;
        this.loc = loc;
        this.machineBlocks = blocks;
    }


    public abstract void createMachine(Player p);

    public abstract void deleteMachine();

    public abstract void onTick(int tick);

    public boolean isMachine(Block block) {
        return getBlocks().contains(block);
    }

    public Location getLoc(){
        return loc;
    }

    public  List<Block> getBlocks(){
        return machineBlocks;
    }

    public void setBlocks(List<Block> blocks){
        machineBlocks = blocks;
    }

    public void addBlock(Block b){
        machineBlocks.add(b);
    }

    public String getName(){
        return name;
    }

    public UUID getUuid(){
        return uuid;
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

    public void removeItemsFuzzy(Inventory inv, Material type, int amount) {
        for (ItemStack is : inv.getContents()) {
            if (is != null && is.getType() == type) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inv.remove(is);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
}
