package cloud.souris.aytos.mc.safari.listeners;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cloud.souris.aytos.mc.safari.areas.Area;
import cloud.souris.aytos.mc.safari.areas.AreaBounds;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.entity.EntitySpawnEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import nukkitcoders.mobplugin.entities.BaseEntity;
import ru.nukkitx.forms.elements.CustomForm;

import java.util.ArrayList;
import java.util.List;

import static cloud.souris.aytos.mc.safari.areas.Area.isOwner;

public class AreasListener implements Listener {
    private final SafariPlugin instance;

    public AreasListener(SafariPlugin instance) {
        this.instance = instance;
    }

    // Entity Events

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobSpawn(EntitySpawnEvent event) {
        Area area = instance.getAreaByPosition(event.getPosition());
        if (area != null) {
            if (area.isAllowed("disableMobs")) {
                if (event.getEntity() instanceof EntityMob || event.getEntity() instanceof BaseEntity) {
                    instance.getLogger().info("area -- mobspawn disabled here so canceling entity");
                    event.getEntity().close();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(EntityExplodeEvent event) {
        List<Block> blocksWhereExplode = new ArrayList<>();
        event.getBlockList().forEach(block -> {
            Area area = instance.getAreaByPosition(block.getLocation());
            if (area == null) {
                blocksWhereExplode.add(block);
            }
        });
        event.setBlockList(blocksWhereExplode);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Area area = instance.getAreaByPosition(event.getEntity().getPosition());
            if (area != null) {
                if (isOwner((Player) event.getEntity(), area)) {
                    event.setCancelled(true);
                    Area.notify(instance, (Player) event.getEntity(), "Tady nemuzes utrpet zraneni, jsi doma v '" + area.getName() + "'");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPvp(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            Area area = instance.getAreaByPosition(player.getPosition());
            if (area != null) {
                if (!isOwner(damager, area)) {
                    event.setCancelled(true);
                    Area.notify(instance, damager, "Tady nejde PvP! Jsi na navsteve v '" + area.getName() + "'");
                }
            } else { // block-o-block utok by fungoval kdyby to tu nebylo
                Area dmgarea = instance.getAreaByPosition(damager.getPosition());
                if (dmgarea != null) {
                    if (!isOwner(damager, dmgarea)) {
                        event.setCancelled(true);
                        Area.notify(instance, damager, "Tady nejde PvP! Jsi na navsteve v '" + area.getName() + "'");
                    }
                }
            }
        }
    }

    // Player Events

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event) {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Area area = instance.getAreaByPosition(event.getBlock().getLocation());
        if (area != null) {
            if (!isOwner(event.getPlayer(), area)) {
                event.setCancelled(true);
                Area.notify(instance, event.getPlayer(), "Tady nemuzes nic delat, jsi na navsteve v '" + area.getName() + "'");
            }
        }
    }

    // Block Events

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event) {
        Area area = instance.getAreaByPosition(event.getBlock().getLocation());
        if (area != null) {
            if (!isOwner(event.getPlayer(), area)) {
                event.setCancelled(true);
                Area.notify(instance, event.getPlayer(), "Tady nemuzes nic delat, jsi na navsteve v '" + area.getName() + "'");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        Area area = instance.getAreaByPosition(event.getBlock().getLocation());
        if (area != null) {
            if (!isOwner(event.getPlayer(), area)) {
                event.setCancelled(true);
                Area.notify(instance, event.getPlayer(), "Tady nemuzes nic delat, jsi na navsteve v '" + area.getName() + "'");
            }
        }
    }

    // Other Events

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrame(ItemFrameDropItemEvent event) {
        Area area = instance.getAreaByPosition(event.getBlock().getLocation());
        if (area != null) {
            instance.getLogger().info("area -- here is onItemFrame -- so i cancel if not by owner");
            if (!Area.isOwner(event.getPlayer(), area)) {
                event.setCancelled(true);
            }
        }
    }

    // Statics (invoked by common events)
    public static void hoeEvent(SafariPlugin instance, PlayerInteractEvent event) {
        event.setCancelled(true);

        Area existingArea = instance.getAreaByPosition(event.getPlayer().getPosition());
        if (existingArea != null) {
            event.getPlayer().sendMessage("Tady už ale rezidence je: " + existingArea.getName());
            event.getPlayer().sendMessage(existingArea.getBounds().toString());
            return;
        }

        AreaBounds bounds = new AreaBounds(event.getPlayer().getPosition().asVector3f().asVector3());

        CustomForm customForm = new CustomForm()
                .setTitle("Vytvoření rezidence")
                .addInput("Název rezidence", "např. 'Tady je moribundus'")
                .addLabel("Rezidence se ti vytvoří v:")
                .addLabel(bounds.toString());

        customForm.send(event.getPlayer(), (targetPlayer, targetForm, data) -> {
            if (data == null) {
                return;
            }

            String areaName = (String) data.get(0);
            if (!areaName.isEmpty()) {
                instance.createArea(areaName, targetPlayer);
                targetPlayer.sendMessage("Rezidence vytvořena");
            }
        });
    }
}
