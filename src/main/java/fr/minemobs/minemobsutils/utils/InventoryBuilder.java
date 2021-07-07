package fr.minemobs.minemobsutils.utils;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class InventoryBuilder implements Listener {

    private int size = 3;
    private final String name;
    private final List<ItemStack> itemStacks;
    private Consumer<InventoryClickEvent> clickEventConsumer = InventoryEvent::getInventory;
    private boolean cancelEvent = false;

    public InventoryBuilder(@NotNull String name) {
        this.name = name;
        this.itemStacks = new ArrayList<>();
    }

    public InventoryBuilder(@NotNull String name, @NotNull int size) {
        this.name = name;
        this.itemStacks = new ArrayList<>();
        this.setSize(size);
    }

    public InventoryBuilder onClick(Consumer<InventoryClickEvent> eventConsumer) {
        this.clickEventConsumer = eventConsumer;
        return this;
    }

    public InventoryBuilder setCancelled() {
        this.cancelEvent = !cancelEvent;
        return this;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if(event.getInventory().getSize() != size) return;
        if(!event.getView().getTitle().equalsIgnoreCase(name)) return;
        event.setCancelled(cancelEvent);
        this.clickEventConsumer.accept(event);
    }

    public InventoryBuilder setSize(int size) {
        if(size > 6) size = 6;
        if(size < 1) size = 3;
        this.size = size;
        return this;
    }

    public InventoryBuilder addItem(ItemStack itemStack) {
        this.itemStacks.add(itemStack);
        return this;
    }

    public InventoryBuilder addItems(ItemStack... itemStacks) {
        this.itemStacks.addAll(Arrays.asList(itemStacks));
        return this;
    }

    public Inventory build() {
        Bukkit.getServer().getPluginManager().registerEvents(this, MinemobsUtils.getInstance());
        Inventory inv = Bukkit.createInventory(null, size * 9, name);
        itemStacks.forEach(inv::addItem);
        return inv;
    }

}