package dev.AidenKR.InventoryMenuAPI.command;

import dev.AidenKR.InventoryMenuAPI.InventoryMenuAPI;
import dev.AidenKR.InventoryMenuAPI.InventoryMenuType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestInventoryMenuCommand extends Command {

    public TestInventoryMenuCommand() {
        super("invtest");
        this.setPermission("op");
        this.setUsage("/invtest <type>");
        this.setDescription("InventoryMenuAPI의 모든 인벤토리 타입을 테스트합니다.");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Arrays.stream(InventoryMenuType.values())
                    .map(t -> t.name().toLowerCase())
                    .filter(name -> name.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            String types = Arrays.stream(InventoryMenuType.values())
                    .map(t -> t.name().toLowerCase())
                    .collect(Collectors.joining(", "));
            InventoryMenuType type;
            InventoryMenuAPI api;

            if (args.length < 1) {
                player.sendMessage("§l§a[!] §r§7사용법 : /invtest <type>");
                player.sendMessage("§l§a[!] §r§7사용 가능한 타입 : " + types);
                return true;
            }

            try {
                type = InventoryMenuType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage("§l§c[!] §r§7존재하지 않는 타입. " + args[0]);
                return true;
            }

            if (type.name().equals("CHEST_6_ROW")) {
                api = InventoryMenuAPI.createCustomGUI(type, "space.-16", "\uE001");
            } else {
                String title = "§0" + type.name() + " TEST";
                api = InventoryMenuAPI.create(type, title);
            }

            for (int i = 0; i < type.getSize(); i++) {
                try {
                    ItemStack item = new ItemStack(Material.PAPER);
                    ItemMeta meta = item.getItemMeta();

                    if (meta != null) {
                        meta.displayName(Component.text("§b" + i + "번 슬롯"));
                    }
                    item.setItemMeta(meta);
                    api.getInventory().setItem(i, item);
                } catch (Exception ignored) {}
            }
            api.setListener((p, item, slot) -> {
                String itemName = (item != null && item.getType() != Material.AIR) ? item.getType().name() : "AIR";
                p.sendMessage("§e[Click] §f슬롯: §b" + slot + " §f| 아이템: §7" + itemName + " §f| 타입: §a" + type.name());
                return true;
            });

            api.setCloseListener(p -> {
                p.sendMessage("창을 닫았습니다.");
            });
            api.send(player);
        }
        return true;
    }
}