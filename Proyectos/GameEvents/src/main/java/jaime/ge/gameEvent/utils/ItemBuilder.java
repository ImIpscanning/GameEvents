package jaime.ge.gameEvent.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;


public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta meta;

    private ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.meta = itemStack.getItemMeta();
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public ItemBuilder name(Component name) {
        meta.displayName(name);
        return this;
    }

    /**
      * @param name Non-colorized name
     **/
    public ItemBuilder name(String name) {
        String content = colorize(name);
        TextComponent textComponent = LegacyComponentSerializer.legacySection().deserialize(content);
        meta.displayName(textComponent);
        return this;
    }

    public ItemBuilder customModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public List<Component> getLore(){
        return meta.lore();
    }

    public ItemBuilder addLine(String line){
        String content = colorize(line);
        TextComponent textComponent = LegacyComponentSerializer.legacySection().deserialize(content);

        List<Component> lore = meta.lore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(textComponent);
        meta.lore(lore);

        return this;
    }

    public ItemBuilder hideAll(){
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_STORED_ENCHANTS
        );
        return this;
    }


    public ItemBuilder removeLastLine(){
        List<Component> lore = meta.lore();
        if (lore == null || lore.isEmpty()) {
            return this;
        }
        lore.removeLast();
        meta.lore(lore);

        return this;
    }
}
