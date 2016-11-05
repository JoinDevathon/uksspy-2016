package org.devathon.contest2016.machines;

import com.google.common.collect.ImmutableMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.devathon.contest2016.Machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Packager extends Machine {

    private static final HashMap<ItemStack, Material> packagerItems = new HashMap<ItemStack, Material>(){{
        put(new ItemStack(Material.IRON_INGOT, 9), Material.IRON_BLOCK);
        put(new ItemStack(Material.GOLD_INGOT, 9), Material.GOLD_BLOCK);
        put(new ItemStack(Material.DIAMOND, 9), Material.DIAMOND_BLOCK);
        put(new ItemStack(Material.EMERALD, 9), Material.EMERALD_BLOCK);
        put(new ItemStack(Material.COAL, 9), Material.COAL_BLOCK);
        put(new ItemStack(Material.INK_SACK, 9, (byte) 4), Material.LAPIS_BLOCK);
        put(new ItemStack(Material.REDSTONE, 9), Material.REDSTONE_BLOCK);
        put(new ItemStack(Material.CLAY_BALL, 4), Material.CLAY);
        put(new ItemStack(Material.STRING, 9), Material.WOOL);
        put(new ItemStack(Material.WHEAT, 9), Material.HAY_BLOCK);
        put(new ItemStack(Material.NETHER_BRICK_ITEM, 4), Material.NETHER_BRICK);

    }};

    private String name;
    private Location loc;
    private List<Block> machineBlocks = new ArrayList<>();

    private Inventory inputInv;
    private Inventory outputInv;

    /**
     * General constructor to be used to store the machines in the Plugin instance
     */
    public Packager(){
        name = "Packager";
        loc = null;
    }

    public Packager(Location loc){
        super(loc);
        this.loc = loc;
    }

    /**
     * Unfortunately, this does not adhere to any protection plugins yet (e.g. WorldGuard).
     */
    public void createMachine(Player p) {
        Block b = loc.getBlock();

        b.setType(Material.DISPENSER);
        outputInv = ((Dispenser) b.getState()).getInventory();
        machineBlocks.add(b);

        b = b.getRelative(BlockFace.UP);

        b.setType(Material.PISTON_BASE);
        b.setData((byte) 0);
        machineBlocks.add(b);

        b = b.getRelative(BlockFace.UP);

        b.setType(Material.HOPPER);
        inputInv = ((Hopper) b.getState()).getInventory();
        machineBlocks.add(b);
    }

    public void deleteMachine(){
        for(Block block : machineBlocks) block.setType(Material.AIR);

        ItemStack itemStack = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Packager");
        itemStack.setItemMeta(im);
        loc.getWorld().dropItemNaturally(loc, itemStack);
    }

    public void onTick(int tick){
        if(tick % 20 == 0){ //Run every second
            for(ItemStack is : packagerItems.keySet()){
                if(inputInv.containsAtLeast(is,is.getAmount())){

                    //Place result in output Inv
                    HashMap<Integer,ItemStack> leftover = outputInv.addItem(new ItemStack(packagerItems.get(is),1));

                    inputInv.removeItem(is);
                    if(leftover.isEmpty()){
                        loc.getWorld().playSound(loc, Sound.BLOCK_PISTON_CONTRACT, 1.0F, 0.7F);
                        loc.getWorld().playSound(loc, Sound.BLOCK_ANVIL_LAND, 0.5F, 1.3F);
                    }else{
                        inputInv.addItem(is);
                    }

                    break; //Preform only one operation
                }
            }
        }
    }

    @Override
    public boolean isMachine(Block block) {
        return machineBlocks.contains(block);
    }

    @Override
    public String getName() {
        return name;
    }
}
