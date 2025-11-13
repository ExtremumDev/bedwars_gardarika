package me.gardarika.bedwars.core.environment;

import me.gardarika.bedwars.core.items.ResourceType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ResourceSpawner {
    private final ResourceType resourceType;
    private final Location spawnerLocation;
    private final ItemStack spawningItemStack;
    private final World gameWorld;

    public ResourceSpawner(ResourceType resourceType, Location spawnerLocation){
        this.resourceType = resourceType;
        this.spawnerLocation = spawnerLocation;
        this.gameWorld = spawnerLocation.getWorld();

        this.spawningItemStack = new ItemStack(resourceType.getMaterial());
        ItemMeta itemMeta = this.spawningItemStack.getItemMeta();
        itemMeta.displayName(Component.text(resourceType.getDisplayName()));
        this.spawningItemStack.setItemMeta(itemMeta);
    }

    public void dropResource(){
        gameWorld.dropItemNaturally(spawnerLocation, this.spawningItemStack);
    }
}
