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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Unpacker extends Machine{

    private static final HashMap<Material, ItemStack> unpackerItems = new HashMap<Material, ItemStack>(){{
        put(Material.IRON_BLOCK, new ItemStack(Material.IRON_INGOT, 9));
        put(Material.GOLD_BLOCK, new ItemStack(Material.GOLD_INGOT, 9));
        put(Material.DIAMOND_BLOCK, new ItemStack(Material.DIAMOND, 9));
        put(Material.EMERALD_BLOCK, new ItemStack(Material.EMERALD, 9));
        put(Material.COAL_BLOCK, new ItemStack(Material.COAL, 9));
        put(Material.LAPIS_BLOCK, new ItemStack(Material.INK_SACK, 9, (byte) 4));
        put(Material.REDSTONE_BLOCK, new ItemStack(Material.REDSTONE, 9));
        put(Material.CLAY, new ItemStack(Material.CLAY_BALL, 4));
        put(Material.WOOL, new ItemStack(Material.STRING, 9));
        put(Material.HAY_BLOCK, new ItemStack(Material.WHEAT, 9));
        put(Material.NETHER_BRICK, new ItemStack(Material.NETHER_BRICK_ITEM, 4));

    }};

    private String name;

    private Inventory inputInv;
    private Inventory outputInv;

    /**
     * General constructor to be used to store the machines in the Plugin instance
     */
    public Unpacker(){
        name = "Unpacker";
    }

    public Unpacker(UUID uuid, Location loc){
        super(uuid, loc);
    }

    public Unpacker(UUID uuid, Location loc, List<Block> blocks){
        super(uuid,loc,blocks);
        createMachine(null);
    }

    /**
     * Unfortunately, this does not adhere to any protection plugins yet (e.g. WorldGuard).
     */
    public void createMachine(Player p) {
        Block b = getLoc().getBlock();

        b.setType(Material.DISPENSER);
        outputInv = ((Dispenser) b.getState()).getInventory();
        addBlock(b);

        b = b.getRelative(BlockFace.UP);

        b.setType(Material.PISTON_STICKY_BASE);
        b.setData((byte) 1);
        addBlock(b);

        b = b.getRelative(BlockFace.UP);

        b.setType(Material.HOPPER);
        inputInv = ((Hopper) b.getState()).getInventory();
        addBlock(b);
    }

    public void deleteMachine(){
        for(Block block : getBlocks()) block.setType(Material.AIR);

        ItemStack itemStack = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Unpacker");
        itemStack.setItemMeta(im);
        getLoc().getWorld().dropItemNaturally(getLoc(), itemStack);
    }

    public void onTick(int tick){
        if(tick % 20 == 0){ //Run every second
            for(Material m : unpackerItems.keySet()){
                if(inputInv.containsAtLeast(new ItemStack(m,1),1)){

                    if(!hasRoom(outputInv, unpackerItems.get(m).clone())) return;

                    inputInv.removeItem(new ItemStack(m,1));
                    System.out.println(unpackerItems.get(m));
                    outputInv.addItem(unpackerItems.get(m).clone());

                    getLoc().getWorld().playSound(getLoc(), Sound.BLOCK_PISTON_EXTEND, 1.0F, 1.3F);
                    getLoc().getWorld().playSound(getLoc(), Sound.BLOCK_ANVIL_LAND, 0.5F, 0.7F);

                    break; //Preform only one operation
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}