# InventoryMenuAPI

A lightweight inventory GUI API for Paper Minecraft servers. Create menus, handle slot clicks, control item movement, and display resource pack-based GUI titles using Adventure components.

## Features

- Simple menu creation with `InventoryMenuAPI.create(...)`
- Built-in inventory types via `InventoryMenuType`
- Slot click callbacks with a boolean cancellation result
- Optional item movement through `setMovable(boolean)`
- Menu-close callbacks
- Custom GUI titles using resource pack translation keys and glyphs
- Optional namespaced font keys for custom resource pack fonts
- Inventory access for setting menu contents

## Requirements

- Java 21
- Paper API 1.21.x (the project currently compiles against `1.21-R0.1-SNAPSHOT`)
- Gradle Wrapper (included)

> Compatibility with every Paper 1.21.x release and every `InventoryMenuType` has not been verified. Some special inventory types may not support programmatic creation.

## Build

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

The project's `shadowJar` task names the output `InventoryMenuAPI.jar`. **Note:** the current `build.gradle.kts` sets its destination to `/Users/USER/Desktop/minecraft/paper/plugins`. Remove that `destinationDirectory` override to use Gradle's standard `build/libs` directory.

Place the compiled JAR in the Paper server's `plugins/` directory and restart the server.

For a plugin that uses this API, add `InventoryMenuAPI` as a plugin dependency in your plugin's `plugin.yml`, and add the API JAR to your development project's compile classpath.

```yaml
depend: [InventoryMenuAPI]
```

## Usage

### Create and open a menu

```java
import dev.AidenKR.InventoryMenuAPI.InventoryMenuAPI;
import dev.AidenKR.InventoryMenuAPI.InventoryMenuType;

InventoryMenuAPI menu = InventoryMenuAPI.create(
        InventoryMenuType.DOUBLE_CHEST,
        "My Menu"
);

menu.send(player);
```

### Set items and handle clicks

```java
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

InventoryMenuAPI menu = InventoryMenuAPI.create(InventoryMenuType.CHEST, "Example Menu");
menu.getInventory().setItem(13, new ItemStack(Material.DIAMOND));

menu.setListener((player, item, slot) -> {
    if (slot == 13) {
        player.sendMessage("You clicked the diamond!");
    }
    return true; // Cancel the clicked slot's default inventory action
});

menu.send(player);
```

Click callbacks run for **top-inventory slots**. Returning `true` cancels that click; returning `false` does not cancel it on its own.

### Allow item movement

```java
InventoryMenuAPI menu = InventoryMenuAPI.create(InventoryMenuType.DOUBLE_CHEST, "Trash");
menu.setMovable(true);
menu.setCloseListener(player -> menu.getInventory().clear());
menu.send(player);
```

By default, `movable` is `false`, so the listener blocks direct actions on the top menu, and protects it against shift-transfer, cursor collection, and drag actions. Normal interactions in the player's own inventory remain available, except for shift-transfer and collect-to-cursor actions when movement is disabled.

**Important:** The example should create a separate menu for each player. A menu's underlying inventory can be shared if the same API object is sent to multiple players. The example deletes its contents when the GUI closes and offers no undo.

### Custom resource pack GUI

Use a resource pack translation key for horizontal spacing and a glyph mapped to a GUI texture:

```java
InventoryMenuAPI menu = InventoryMenuAPI.createCustomGUI(
        InventoryMenuType.DOUBLE_CHEST,
        "space.-16",
        "\uE001"
);
menu.send(player);
```

To select a specific font namespace:

```java
InventoryMenuAPI menu = InventoryMenuAPI.createCustomGUI(
        InventoryMenuType.DOUBLE_CHEST,
        "space.-16",
        "\uE001",
        "custom:gui"
);
menu.send(player);
```

For example, the `custom:gui` font identifier corresponds to `assets/custom/font/gui.json` in the resource pack. The glyph and the `space.-16` translation must be defined in the player's resource pack. The API **does not upload, generate, or automatically apply** resource packs.

### On-close callback

```java
menu.setCloseListener(player -> player.sendMessage("Menu closed"));
```

### Change menu title

```java
import net.kyori.adventure.text.Component;

menu.setName(Component.text("Updated Menu"));
```

The current implementation recreates the backing inventory when changing the title, so change the title only when the inventory is not open.

## API overview

| Method | Purpose |
| --- | --- |
| `create(type)` | Create a menu with an empty title |
| `create(type, name)` | Create a menu with a text title |
| `createCustomGUI(type, spaceKey, textureChar)` | Create a resource pack glyph title |
| `createCustomGUI(type, spaceKey, textureChar, fontKey)` | Create a glyph title with a custom font |
| `getInventory()` | Access the backing Bukkit inventory |
| `setListener(callback)` | Register a slot-click callback |
| `setCloseListener(callback)` | Register a menu-close callback |
| `setMovable(boolean)` | Enable or disable top-menu item movement |
| `setName(component)` | Change the title while closed |
| `send(player)` | Open the inventory for a player |

## Test command

The plugin currently includes `/invtest <type>` for trying inventory types. It is registered programmatically and restricted to operators. Not all special inventory types have been tested.

## Known limitations

- The current `MenuRegistry.get(String)` implementation constructs a default menu even on successful lookup because it passes `InventoryMenuAPI.create(...)` to `Map.getOrDefault(...)`. This should be fixed before production deployment.
- Created menus are registered globally. The current code does not automatically remove them after their use, which can retain menu instances in memory.
- `setInventory(...)` requires the replacement inventory to belong to the same `MenuInventory` holder. Its open-inventory validation should be reviewed before use.
- The test command checks for `CHEST_6_ROW`, while the enum currently calls the six-row type `DOUBLE_CHEST`; the custom GUI test branch is therefore unreachable.
- Resource pack rendering depends on client-side font and translation definitions.
- Full build and live-server verification are pending.

## License

No license has been selected yet. Add a `LICENSE` file before offering explicit reuse and redistribution rights.
