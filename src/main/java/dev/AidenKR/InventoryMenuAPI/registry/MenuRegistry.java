package dev.AidenKR.InventoryMenuAPI.registry;

import dev.AidenKR.InventoryMenuAPI.InventoryMenuAPI;
import dev.AidenKR.InventoryMenuAPI.InventoryMenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MenuRegistry {

    private static MenuRegistry instance;

    private final Map<String, InventoryMenuAPI> cache = new ConcurrentHashMap<>();

    public static MenuRegistry getInstance() {
        if (instance == null) {
            instance = new MenuRegistry();
        }
        return instance;
    }

    private MenuRegistry() {
        instance = this;
    }

    public Map<String, InventoryMenuAPI> getAll() {
        return cache;
    }

    public InventoryMenuAPI get(String id) {
        return cache.get(id);
    }

    public void register(String id, InventoryMenuAPI api) {
        cache.put(id, api);
    }

    public void unregister(String id) {
        cache.remove(id);
    }
}
