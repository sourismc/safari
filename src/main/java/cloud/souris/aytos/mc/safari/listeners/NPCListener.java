package cloud.souris.aytos.mc.safari.listeners;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cloud.souris.aytos.mc.safari.npcs.HumanNPC;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityVehicleEnterEvent;

public class NPCListener implements Listener {
    private final SafariPlugin instance;

    public NPCListener(SafariPlugin instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof HumanNPC) {
            event.setCancelled(true);
            if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
                Player player = (Player) ((EntityDamageByEntityEvent) event).getDamager();
                player.sendMessage("Tohle je NPC s ID " + entity.getId() + " a jménem " + entity.getName());
            }
        }
    }

    @EventHandler
    public void onEnterVehicle(EntityVehicleEnterEvent event) {
        if (event.getEntity() instanceof HumanNPC) {
            event.setCancelled(true);
        }
    }
}
