package dev.AidenKR.InventoryMenuAPI;

import dev.AidenKR.InventoryMenuAPI.command.TestInventoryMenuCommand;
import dev.AidenKR.InventoryMenuAPI.listener.EventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        getServer().getCommandMap().register(getName(), new TestInventoryMenuCommand());
    }
}


