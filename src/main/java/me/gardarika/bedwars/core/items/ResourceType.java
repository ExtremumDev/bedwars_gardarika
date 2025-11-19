package me.gardarika.bedwars.core.items;

import org.bukkit.Material;

public enum ResourceType {
    BRONZE("Бронза", Material.BRICK, 1 * 20),
    IRON("Железо", Material.IRON_INGOT, 15 * 20),
    GOLD("Золотой червонец", Material.GOLD_INGOT, 30 * 20),
    DIAMOND("Сапфир", Material.DIAMOND, 60 * 20);

    private final String displayName;
    private final Material material;
    private final int defaultSpawnInterval;

    ResourceType(String displayName, Material material, int defaultSpawnInterval){
        this.displayName = displayName;
        this.material = material;
        this.defaultSpawnInterval = defaultSpawnInterval;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public int getDefaultSpawnInterval() {
        return defaultSpawnInterval;
    }
}
