package cloud.souris.aytos.mc.safari.listeners;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cloud.souris.aytos.mc.safari.areas.Area;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.entity.EntitySpawnEvent;
import cn.nukkit.utils.TextFormat;

public class EntityListener implements Listener {
    private final SafariPlugin instance;

    public EntityListener(SafariPlugin instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobSpawn(EntitySpawnEvent event) {
        Area area = instance.getAreaByPosition(event.getPosition());
        if (area != null) {
            if (area.isAllowed("disableMobs")) {
                if (event.getEntity() instanceof EntityMob) {
                    instance.getLogger().info("area -- mobspawn disabled here so canceling entity");
                    event.getEntity().close();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrame(ItemFrameDropItemEvent event) {
        Area area = instance.getAreaByPosition(event.getBlock().getLocation());
        if (area != null) {
            instance.getLogger().info("area -- here is onItemFrame -- so i cancel? ok?" + TextFormat.DARK_PURPLE + "[for test its disabled so check what this is]");
        }
    }
}
