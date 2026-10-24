package dev.AidenKR.InventoryMenuAPI.inventory;

import dev.AidenKR.InventoryMenuAPI.InventoryMenuAPI;
import dev.AidenKR.InventoryMenuAPI.InventoryMenuType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MenuInventory implements InventoryHolder {

    private Inventory inventory;

    private final InventoryMenuAPI owner;
    private final InventoryMenuType type;

    public MenuInventory(InventoryMenuAPI owner, InventoryMenuType type, Component title) {
        this.owner = owner;
        this.type =type;
        this.inventory = create(title);
    }

    private Inventory create(Component title) {
        if (type.getDefaultType() == InventoryType.CHEST) {
            return Bukkit.createInventory(this, type.getSize(), title);
        }
        return Bukkit.createInventory(this, type.getDefaultType(), title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public @NotNull InventoryMenuAPI getOwner() {
        return this.owner;
    }

    public void setInventory(Inventory inventory) {
        Objects.requireNonNull(inventory, "inventory replacement");

        if (inventory.getHolder() != this) {
            throw new IllegalArgumentException("Replacement inventory must use the original MenuInventory");
        }

        if (!this.inventory.getViewers().isEmpty()) {
            throw new IllegalStateException("Cannot replace an open inventory");
        }
        this.inventory = inventory;
    }

    public void updateTitle(Component title) {
        if (!inventory.getViewers().isEmpty()) {
            throw new IllegalStateException("Cannot change the title while menu is open");
        }
        Inventory replacement = create(title);
        replacement.setContents(inventory.getContents());
        this.inventory = replacement;
    }
}