package dev.AidenKR.InventoryMenuAPI;

import org.bukkit.event.inventory.InventoryType;

public enum InventoryMenuType {

    CHEST(27, "Chest", InventoryType.CHEST),
    DOUBLE_CHEST(54, "Double Chest", InventoryType.CHEST),
    HOPPER(5, "Item Hopper", InventoryType.HOPPER),
    DROPPER(9, "Dropper", InventoryType.DROPPER),
    DISPENSER(9, "Dispenser", InventoryType.DISPENSER),
    ANVIL(3, "Repairing", InventoryType.ANVIL),
    FURNACE(3, "Furnace", InventoryType.FURNACE),
    WORKBENCH(10, "Crafting", InventoryType.WORKBENCH),
    BREWING(5, "Brewing", InventoryType.BREWING),

    ENCHANTING(2, "Enchanting", InventoryType.ENCHANTING),
    ENDER_CHEST(27, "Ender Chest", InventoryType.ENDER_CHEST),
    SMITHING(4, "Upgrade Gear", InventoryType.SMITHING),
    BEACON(1, "Beacon", InventoryType.BEACON),
    SHULKER_BOX(27, "Shulker Box", InventoryType.SHULKER_BOX),
    BARREL(27, "Barrel", InventoryType.BARREL),
    BLAST_FURNACE(3, "Blast Furnace", InventoryType.BLAST_FURNACE),
    LECTERN(1, "Lectern", InventoryType.LECTERN),
    SMOKER(3, "Smoker", InventoryType.SMOKER),
    LOOM(4, "Loom", InventoryType.LOOM),
    CARTOGRAPHY(3, "Cartography Table", InventoryType.CARTOGRAPHY),
    GRINDSTONE(3, "Repair & Disenchant", InventoryType.GRINDSTONE),
    STONECUTTER(2, "Stonecutter", InventoryType.STONECUTTER),
    CRAFTER(9, "Crafter", InventoryType.CRAFTER);

    private final int size;
    private final String defaultTitle;
    private final InventoryType defaultType;

    InventoryMenuType(int size, String defaultTitle, InventoryType defaultType) {
        this.size = size;
        this.defaultTitle = defaultTitle;
        this.defaultType = defaultType;
    }

    public int getSize() {
        return size;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public InventoryType getDefaultType() {
        return defaultType;
    }
}