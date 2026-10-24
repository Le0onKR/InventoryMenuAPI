package dev.AidenKR.InventoryMenuAPI.listener;

import dev.AidenKR.InventoryMenuAPI.InventoryMenuAPI;
import dev.AidenKR.InventoryMenuAPI.inventory.MenuInventory;
import dev.AidenKR.InventoryMenuAPI.transaction.InventoryMenuTransaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class EventListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        InventoryHolder holder = top.getHolder();

        if (holder instanceof MenuInventory inventory) {
            InventoryMenuAPI menu = inventory.getOwner();
            int slot = event.getRawSlot();
            boolean topClick = slot >= 0 && slot < top.getSize();

            if (event.getWhoClicked() instanceof Player player) {
                if (!menu.isMovable()) {
                    if (topClick || event.getAction() == InventoryAction.COLLECT_TO_CURSOR || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        event.setCancelled(true);
                    }
                }

                if (topClick) {
                    InventoryMenuTransaction listener = menu.getListener();

                    if (listener != null) {
                        if (listener.onClick(player, event.getCurrentItem(), slot)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Inventory top = event.getView().getTopInventory();

        if (top.getHolder() instanceof MenuInventory inventory) {
            InventoryMenuAPI menu = inventory.getOwner();

            if (menu.isMovable()) return;

            for (int slot : event.getRawSlots()) {
                if (slot < top.getSize()) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof MenuInventory inventory) {
            if (event.getPlayer() instanceof Player player) {
                inventory.getOwner().onClose(player);
            }
        }
    }
}