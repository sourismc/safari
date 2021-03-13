package cloud.souris.aytos.mc.safari.listeners;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cloud.souris.aytos.mc.safari.areas.Area;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {
    private final SafariPlugin instance;

    public BlockListener(SafariPlugin instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Area area = instance.getAreaByPosition(event.getBlock().getLocation());
        if (area != null) {
            if (event.getPlayer().getUniqueId() != area.getOwnerId()) {
                event.setCancelled(true);
                notify(event.getPlayer(), "Tady nemuzes nic delat, jsi na navsteve v '" + area.getName() + "'");
            }
        }
    }

    public void notify(Player player, String message) {
        Long now = System.currentTimeMillis();
        if (instance.areaMessageCooldowns.containsKey(player.getUniqueId())) {
            Long cd = instance.areaMessageCooldowns.get(player.getUniqueId());
            long diff = now - cd;
            instance.areaMessageCooldowns.put(player.getUniqueId(), now);
            if (diff > 2500) {
                player.sendMessage(message);
            }
        } else {
            instance.areaMessageCooldowns.put(player.getUniqueId(), now);
            player.sendMessage(message);
        }
    }
}
