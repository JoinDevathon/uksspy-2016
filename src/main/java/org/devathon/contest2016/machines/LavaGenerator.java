package org.devathon.contest2016.machines;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LavaGenerator extends Machine{

    private static final HashMap<Material, Material> lavaItems = new HashMap<Material, Material>(){{
        put(Material.WATER_BUCKET, Material.LAVA_BUCKET);
        put(Material.MILK_BUCKET, Material.LAVA_BUCKET);
    }};

    private String name;
    private UUID uuid;

    private Inventory inputInv;
    private Inventory outputInv;

    /**
     * General constructor to be used to store the machines in the Plugin instance
     */
    public LavaGenerator(){
        name = "LavaGenerator";
    }

    public LavaGenerator(UUID uuid, Location loc){
        super(uuid, loc);
    }

    public LavaGenerator(UUID uuid, Location loc, List<Block> blocks){
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
        b.setData((byte) 1);
        addBlock(b);

        b = b.getRelative(BlockFace.UP);

        b.setType(Material.MAGMA);
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
        im.setDisplayName(ChatColor.BLUE + "LavaGenerator");
        itemStack.setItemMeta(im);
        getLoc().getWorld().dropItemNaturally(getLoc(), itemStack);
    }

    public void onTick(int tick){
        if(tick % 600 == 0){ //Run every 30 seconds
            for(Material m : lavaItems.keySet()){
                if(inputInv.containsAtLeast(new ItemStack(m,1),1)){

                    //Place result in output Inv
                    HashMap<Integer,ItemStack> leftover = outputInv.addItem(new ItemStack(lavaItems.get(m),1));

                    inputInv.removeItem(new ItemStack(m,1));
                    if(leftover.isEmpty()){
                        getLoc().getWorld().playSound(getLoc(), Sound.BLOCK_LAVA_POP, 1.0F, 1.0F);
                        getLoc().getWorld().playSound(getLoc(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0F, 0.6F);
                        getLoc().getWorld().spigot().playEffect(getLoc().clone().add(0.5,1.5,0.5), Effect.LAVADRIP, 0, 0, 0.5F, 0.5F, 0.5F, 0.1F, 10, 16);
                    }else{
                        inputInv.addItem(new ItemStack(m,1));
                    }

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
