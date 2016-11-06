package org.devathon.contest2016.machines;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.devathon.contest2016.Machine;

import java.util.List;
import java.util.UUID;

public class Drainer extends Machine {

    private String name;
    private UUID uuid;

    private Inventory inputInv;
    private Inventory outputInv;

    /**
     * General constructor to be used to store the machines in the Plugin instance
     */
    public Drainer(){
        name = "Drainer";
    }

    public Drainer(UUID uuid, Location loc){
        super(uuid, loc);
    }

    public Drainer(UUID uuid, Location loc, List<Block> blocks){
        super(uuid,loc,blocks);
        createMachine(null);
    }

    @Override
    public void createMachine(Player p) {
        Block b = getLoc().getBlock();

        b.setType(Material.DISPENSER);
        outputInv = ((Dispenser) b.getState()).getInventory();
        b.setData((byte) 1);
        addBlock(b);

        b = b.getRelative(BlockFace.UP);

        b.setType(Material.SPONGE);
        addBlock(b);

        b = b.getRelative(BlockFace.UP);

        b.setType(Material.HOPPER);
        inputInv = ((Hopper) b.getState()).getInventory();
        addBlock(b);
    }

    @Override
    public void deleteMachine() {
        for(Block block : getBlocks()) block.setType(Material.AIR);

        ItemStack itemStack = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Drainer");
        itemStack.setItemMeta(im);
        getLoc().getWorld().dropItemNaturally(getLoc(), itemStack);
    }

    @Override
    public void onTick(int tick) {
        if(tick % 40 == 0){ //Run every 2 seconds
            if(inputInv.contains(Material.POTION)){

                if(!hasRoom(outputInv, new ItemStack(Material.GLASS_BOTTLE, 1))) return;

                for(int slot = 0; slot < inputInv.getSize(); slot++){
                    if(inputInv.getItem(slot) != null && inputInv.getItem(slot).getType() == Material.POTION){
                        inputInv.setItem(slot, null);
                    }
                }
                outputInv.addItem(new ItemStack(Material.GLASS_BOTTLE, 1));

                getLoc().getWorld().playSound(getLoc(), Sound.ITEM_BOTTLE_FILL, 1.0F, 1.3F);
                getLoc().getWorld().playSound(getLoc(), Sound.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
