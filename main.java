package mrfix1033.understanding_unknown;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class main extends JavaPlugin implements CommandExecutor, Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    HashMap<UUID, BukkitTask> g = new HashMap<>();

    @EventHandler
    public void c(PlayerInteractEvent e) {
        Location loc = e.getPlayer().getLocation();
        Material[] t = Material.values();
        for (int i = 0; i < 500; i++) {
            Bukkit.getScheduler().runTaskLater(this, () -> {
                ArmorStand a = (ArmorStand) loc.getWorld().spawnEntity(e.getPlayer().getLocation().add(0, 0.3, 0), EntityType.ARMOR_STAND);
                ((CraftArmorStand) a).getHandle().setInvisible(true);
                ItemStack item = new ItemStack(t[new Random().nextInt(t.length)]);
                while (item.getType() == Material.AIR) item = new ItemStack(t[new Random().nextInt(t.length)]);
                a.setHelmet(item);
                double x = new Random().nextDouble(), y = new Random().nextDouble(), z = new Random().nextDouble();
                a.setVelocity(new Vector(Double.parseDouble(new String[]{"-", "+"}[new Random().nextInt(2)] + x),
                        y, Double.parseDouble(new String[]{"-", "+"}[new Random().nextInt(2)] + z)));
                ItemStack finalItem = item;
                UUID uuid = a.getUniqueId();
                g.put(uuid, Bukkit.getScheduler().runTaskTimer(this, () -> {
                    if (!a.isOnGround()) return;
                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        Item drop = loc.getWorld().dropItemNaturally(a.getLocation(), finalItem);
                        drop.setPickupDelay(0);
                        drop.setGlowing(true);
                        a.remove();
                    }, 50);
                    g.remove(uuid).cancel();
                }, 10, 10));
            }, i);
        }
    }
}
