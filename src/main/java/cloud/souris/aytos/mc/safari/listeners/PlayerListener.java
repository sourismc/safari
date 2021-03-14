package cloud.souris.aytos.mc.safari.listeners;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;

import static cloud.souris.aytos.mc.safari.listeners.AreasListener.hoeEvent;

public class PlayerListener implements Listener {
    private final SafariPlugin instance;

    public PlayerListener(SafariPlugin instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(
                TextFormat.DARK_GRAY + "[" + TextFormat.GREEN + "+" + TextFormat.DARK_GRAY + "] " +
                TextFormat.WHITE + event.getPlayer().getName());

        event.getPlayer().sendTitle(
                TextFormat.GREEN + "Vítej na " + TextFormat.WHITE + "Souris" + TextFormat.MINECOIN_GOLD + "MC",
                TextFormat.AQUA + "" + TextFormat.ITALIC + "Vývojová verze",
                60,
                40,
                30
        );

        instance.dataProvider.initializePlayer(instance, event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(
                TextFormat.DARK_GRAY + "[" + TextFormat.RED + "-" + TextFormat.DARK_GRAY + "] " +
                TextFormat.WHITE + event.getPlayer().getName());

        instance.removePlayer(event.getPlayer().getUniqueId());
        instance.dataProvider.setPlayerOnlineStatusAsync(instance, event.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            rightClickEvent(event);
        }
    }

    // Event Holders

    private void rightClickEvent(PlayerInteractEvent event) {
        Item inHand = event.getPlayer().getInventory().getItemInHand();
        if (inHand.isHoe() && inHand.getTier() == 1) { // Is wooden hoe
            Long now = System.currentTimeMillis();
            if (instance.hoeCooldowns.containsKey(event.getPlayer().getUniqueId())) {
                Long cd = instance.hoeCooldowns.get(event.getPlayer().getUniqueId());
                long diff = now - cd;
                instance.hoeCooldowns.put(event.getPlayer().getUniqueId(), now);
                if (diff > 2500) {
                    hoeEvent(instance, event);
                }
            } else {
                instance.hoeCooldowns.put(event.getPlayer().getUniqueId(), now);
                hoeEvent(instance, event);
            }
        }
    }
}
