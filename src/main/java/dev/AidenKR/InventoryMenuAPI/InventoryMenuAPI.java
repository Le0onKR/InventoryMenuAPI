package dev.AidenKR.InventoryMenuAPI;

import dev.AidenKR.InventoryMenuAPI.inventory.MenuInventory;
import dev.AidenKR.InventoryMenuAPI.registry.MenuRegistry;
import dev.AidenKR.InventoryMenuAPI.transaction.InventoryMenuTransaction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public final class InventoryMenuAPI {

    private final String uuid = UUID.randomUUID().toString();

    private Component name;
    private final MenuInventory inventory;
    private InventoryMenuTransaction listener;
    private Consumer<Player> closeListener;

    private boolean movable;

    private InventoryMenuAPI(InventoryMenuType type, Component title) {
        this.name = Objects.requireNonNull(title, "title");
        this.inventory = new MenuInventory(this, Objects.requireNonNull(type, "type"), title);
        MenuRegistry.getInstance().register(uuid, this);
    }

    public static InventoryMenuAPI create(InventoryMenuType type) {
        return new InventoryMenuAPI(type, Component.text(""));
    }

    public static InventoryMenuAPI create(InventoryMenuType type, String name) {
        return new InventoryMenuAPI(type, Component.text(name));
    }

    public static InventoryMenuAPI createCustomGUI(InventoryMenuType type, String spaceKey, String textureChar) {
        Component title = Component.translatable(spaceKey).append(Component.text(textureChar));
        return new InventoryMenuAPI(type, title);
    }

    public static InventoryMenuAPI createCustomGUI(InventoryMenuType type, String spaceKey, String textureChar, String fontKey) {
        Key font = Key.key(Objects.requireNonNull(fontKey, "fontKey"));
        Component title = Component.translatable(spaceKey).append(Component.text(textureChar).font(font));
        return new InventoryMenuAPI(type, title);
    }

    public String getName() {
        return PlainTextComponentSerializer.plainText().serialize(name);
    }

    public void setName(Component name) {
        Objects.requireNonNull(name, "name");
        inventory.updateTitle(name);
        this.name = name;
    }

    public Component getNameComponent() {
        return name;
    }

    public String getUUID() {
        return uuid;
    }

    public boolean isMovable() {
        return movable;
    }

    public Inventory getInventory() {
        return inventory.getInventory();
    }

    public InventoryMenuTransaction getListener() {
        return listener;
    }

    public Consumer<Player> getCloseListener() {
        return closeListener;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void setInventory(Inventory replacement) {
        inventory.setInventory(Objects.requireNonNull(replacement, "inventory"));
    }

    public void setListener(InventoryMenuTransaction transaction) {
        this.listener = transaction;
    }

    public void setCloseListener(Consumer<Player> listener) {
        this.closeListener = listener;
    }

    public void onClose(Player player) {
        if (closeListener != null) {
            closeListener.accept(player);
        }
    }

    public void send(Player player) {
        Objects.requireNonNull(player, "player");
        MenuRegistry.getInstance().register(uuid, this);
        player.openInventory(getInventory());
    }

    public void handle(Player player, ItemStack clicked, int slot) {
        if (listener != null) {
            listener.onClick(player, clicked, slot);
        }
    }
}