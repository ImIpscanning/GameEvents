package jaime.ge.gameEvent.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.*;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class ItemUtils {
    public static void serializeInventoryInSection(ConfigurationSection section, ItemStack[] items) {
        for (String key : section.getKeys(false)) {
            section.set(key, null);
        }
        if(items == null){
            return;
        }
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                section.set(String.valueOf(i), items[i].serialize());
            }
        }
    }

    public static ItemStack[] deserializeInventory(ConfigurationSection section){
        if (section == null) {
            return null;
        }

        List<ItemStack> itemList = new ArrayList<>();

        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection != null) {
                Map<String, Object> serializedItem = itemSection.getValues(false);
                ItemStack item = ItemStack.deserialize(serializedItem);
                itemList.add(item); // Agregamos a la lista
            }
        }

        // Convertimos la lista en un array
        return itemList.toArray(new ItemStack[0]);
    }


    public static List<ItemStack> getCraftingIngredients(Material material) {
        List<ItemStack> ingredients = new ArrayList<>();

        Iterator<Recipe> it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe recipe = it.next();
            if (!recipe.getResult().getType().equals(material)) continue;

            if (recipe instanceof ShapedRecipe shaped) {
                Map<Character, ItemStack> ingredientMap = shaped.getIngredientMap();
                String[] shape = shaped.getShape();
                Map<Material, Integer> countMap = new HashMap<>();

                for (String row : shape) {
                    for (char c : row.toCharArray()) {
                        ItemStack item = ingredientMap.get(c);
                        if (item != null && item.getType() != Material.AIR) {
                            countMap.merge(item.getType(), 1, Integer::sum);
                        }
                    }
                }

                for (Map.Entry<Material, Integer> entry : countMap.entrySet()) {
                    ingredients.add(new ItemStack(entry.getKey(), entry.getValue()));
                }

            } else if (recipe instanceof ShapelessRecipe shapeless) {
                Map<Material, Integer> countMap = new HashMap<>();
                for (ItemStack item : shapeless.getIngredientList()) {
                    countMap.merge(item.getType(), 1, Integer::sum);
                }
                for (Map.Entry<Material, Integer> entry : countMap.entrySet()) {
                    ingredients.add(new ItemStack(entry.getKey(), entry.getValue()));
                }
            }

            break; // Solo primera receta
        }

        return ingredients;
    }


    public static List<Material> getMaterialsFromStringList(List<String> stringList, String gameName){
        List<Material> materials = new ArrayList<>();
        for(String s : stringList){
            Material material = Material.getMaterial(s);
            if(material != null) materials.add(material);
        }

        if(materials.isEmpty()){
            Bukkit.getConsoleSender().sendMessage(colorize("&fGameEvents (" + gameName + ") > This list has no materials: " + stringList));
            Bukkit.getConsoleSender().sendMessage(colorize("&cIf intended, " +
                    "add a valid material and disable the feature that uses it. "));
        }

//        Bukkit.getConsoleSender().sendMessage(colorize("&fGameEvents > &a&lMaterials loaded: " + materials));


        return materials;
    }
}
