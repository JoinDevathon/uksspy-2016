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
        addDrainerRecipe();
        addIncubatorRecipe();
        addLavaGeneratorRecipe();
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

    private void addDrainerRecipe(){
        ItemStack result = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Drainer");
        result.setItemMeta(im);

        ShapedRecipe recipe = new ShapedRecipe(result);

        recipe.shape("BHB","ISI","BDB");

        recipe.setIngredient('B', Material.BUCKET);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('H', Material.HOPPER);
        recipe.setIngredient('S', Material.SPONGE);
        recipe.setIngredient('D', Material.DISPENSER);

        plugin.getServer().addRecipe(recipe);

    }

    private void addIncubatorRecipe(){
        ItemStack result = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Incubator");
        result.setItemMeta(im);

        ShapedRecipe recipe = new ShapedRecipe(result);

        recipe.shape("GHG","dBd","GDG");

        recipe.setIngredient('G', Material.GLASS);
        recipe.setIngredient('d', Material.DIAMOND);
        recipe.setIngredient('H', Material.HOPPER);
        recipe.setIngredient('B', Material.BEACON);
        recipe.setIngredient('D', Material.DISPENSER);

        plugin.getServer().addRecipe(recipe);

    }

    private void addLavaGeneratorRecipe(){
        ItemStack result = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Lava Generator");
        result.setItemMeta(im);

        ShapedRecipe recipe = new ShapedRecipe(result);

        recipe.shape("OHO","mMm","ODO");

        recipe.setIngredient('O', Material.OBSIDIAN);
        recipe.setIngredient('m', Material.MAGMA_CREAM);
        recipe.setIngredient('H', Material.HOPPER);
        recipe.setIngredient('M', Material.MAGMA);
        recipe.setIngredient('D', Material.DISPENSER);

        plugin.getServer().addRecipe(recipe);

    }
}
