package dev.AidenKR.InventoryMenuAPI.transaction;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface InventoryMenuTransaction {
    boolean onClick(Player player, ItemStack clicked, int slot);
}