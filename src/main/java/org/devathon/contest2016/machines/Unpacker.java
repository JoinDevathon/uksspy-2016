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

public class Unpacker extends Machine{

    /*
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
     */
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
    private Location loc;
    private List<Block> machineBlocks = new ArrayList<>();

    private Inventory inputInv;
    private Inventory outputInv;

    /**
     * General constructor to be used to store the machines in the Plugin instance
     */
    public Unpacker(){
        name = "Unpacker";
        loc = null;
    }

    public Unpacker(Location loc){
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

        b.setType(Material.PISTON_STICKY_BASE);
        b.setData((byte) 1);
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
        im.setDisplayName(ChatColor.BLUE + "Unpacker");
        itemStack.setItemMeta(im);
        loc.getWorld().dropItemNaturally(loc, itemStack);
    }

    public void onTick(int tick){
        if(tick % 20 == 0){ //Run every second
            for(Material m : unpackerItems.keySet()){
                if(inputInv.containsAtLeast(new ItemStack(m,1),1)){

                    if(!hasRoom(outputInv, unpackerItems.get(m).clone())) return;

                    inputInv.removeItem(new ItemStack(m,1));
                    System.out.println(unpackerItems.get(m));
                    outputInv.addItem(unpackerItems.get(m).clone());

                    loc.getWorld().playSound(loc, Sound.BLOCK_PISTON_EXTEND, 1.0F, 1.3F);
                    loc.getWorld().playSound(loc, Sound.BLOCK_ANVIL_LAND, 0.5F, 0.7F);

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