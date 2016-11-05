package org.devathon.contest2016;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftingRecipes {

    private UltimateMachines plugin;

    public CraftingRecipes(UltimateMachines plugin){
        this.plugin = plugin;
    }

    public void setupRecipes(){
        addPackagerRecipe();
        addUnpackerRecipe();
    }

    private void addPackagerRecipe(){
        ItemStack result = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Packager");
        result.setItemMeta(im);

        ShapedRecipe recipe = new ShapedRecipe(result);

        recipe.shape("IHI","IPI","IDI");

        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('H', Material.HOPPER);
        recipe.setIngredient('P', Material.PISTON_BASE);
        recipe.setIngredient('D', Material.DISPENSER);

        plugin.getServer().addRecipe(recipe);

    }

    private void addUnpackerRecipe(){
        ItemStack result = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Unpacker");
        result.setItemMeta(im);

        ShapedRecipe recipe = new ShapedRecipe(result);

        recipe.shape("IHI","IPI","IDI");

        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('H', Material.HOPPER);
        recipe.setIngredient('P', Material.PISTON_STICKY_BASE);
        recipe.setIngredient('D', Material.DISPENSER);

        plugin.getServer().addRecipe(recipe);

    }
}
