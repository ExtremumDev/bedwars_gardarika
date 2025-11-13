package me.gardarika.bedwars.core.items;

import org.bukkit.Material;

public enum ResourceType {
    BRONZE("Бронза", Material.BRICK),
    IRON("Железо", Material.IRON_INGOT),
    GOLD("Золотой червонец", Material.GOLD_INGOT),
    DIAMOND("Сапфир", Material.DIAMOND);

    private final String displayName;
    private final Material material;

    ResourceType(String displayName, Material material){
        this.displayName = displayName;
        this.material = material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }
}
