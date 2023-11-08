package net.daechler.fillmyinv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FillMyInv extends JavaPlugin {

    // List of materials that can be given to the player.
    private final List<Material> materials = Arrays.asList(Material.values());
    private final List<Material> tools = Arrays.asList(
            Material.WOODEN_SWORD, Material.WOODEN_SHOVEL, Material.WOODEN_PICKAXE,
            Material.WOODEN_AXE, Material.WOODEN_HOE, Material.STONE_SWORD,
            Material.STONE_SHOVEL, Material.STONE_PICKAXE, Material.STONE_AXE,
            Material.STONE_HOE); // Add more tools as necessary
    private final List<Material> armors = Arrays.asList(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS); // Add more armor types as necessary
    private final Random random = new Random();

    @Override
    public void onEnable() {
        // Display a green message saying "<Plugin Name> has been enabled!"
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + getName() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        // Display a red message saying "<Plugin Name> has been disabled!"
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + getName() + " has been disabled!");
    }

    // Handle commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fillmyinv") && sender instanceof Player) {
            Player player = (Player) sender;
            fillInventoryWithRandomItems(player);
            return true;
        }
        return false;
    }

    // Fill the player's inventory with random items
    private void fillInventoryWithRandomItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear(); // Clear the inventory before filling

        // Fill the armor slots with random armor
        inventory.setHelmet(getRandomItem(armors, true));
        inventory.setChestplate(getRandomItem(armors, true));
        inventory.setLeggings(getRandomItem(armors, true));
        inventory.setBoots(getRandomItem(armors, true));

        // Assign a shield to the off-hand
        inventory.setItemInOffHand(new ItemStack(Material.SHIELD));

        // Fill the hotbar with tools
        for (int i = 0; i < 9; i++) {
            if (i < tools.size()) {
                inventory.setItem(i, getRandomItem(tools, true));
            } else {
                inventory.setItem(i, getRandomItem(materials, false));
            }
        }

        // Fill the rest of the inventory with random items
        for (int i = 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, getRandomItem(materials, false));
        }

        player.updateInventory(); // Update the inventory for the player
    }

    // Get a random item with a chance to apply random enchantments
    private ItemStack getRandomItem(List<Material> materialList, boolean enchant) {
        Material material = materialList.get(random.nextInt(materialList.size()));
        ItemStack item = new ItemStack(material, random.nextInt(material.getMaxStackSize()) + 1);
        if (enchant && random.nextBoolean()) {
            Enchantment enchantment = Enchantment.values()[random.nextInt(Enchantment.values().length)];
            item.addUnsafeEnchantment(enchantment, random.nextInt(enchantment.getMaxLevel()) + 1);
        }
        return item;
    }
}
