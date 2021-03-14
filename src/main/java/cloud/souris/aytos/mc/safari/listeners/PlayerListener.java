package cloud.souris.aytos.mc.safari.listeners;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cloud.souris.aytos.mc.safari.SafariUtils;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookWritten;
import cn.nukkit.item.ItemCompass;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
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

        // asi nefunguje?
        event.getPlayer().sendTitle(
                TextFormat.GREEN + "Vítej na " + TextFormat.WHITE + "Souris" + TextFormat.MINECOIN_GOLD + "MC",
                TextFormat.AQUA + "" + TextFormat.ITALIC + "Vývojová verze");

        SafariUtils.givePlayerIntroBookAsync(event.getPlayer().getInventory());
        instance.dataProvider.initializePlayerAsync(instance, event.getPlayer());
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

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        Vector3 bedPosition = event.getBed().getLocation().floor().asVector3f().asVector3();
        event.getBed().getLevel().setTime(Level.TIME_DAY);
        event.setCancelled(true);
        event.getPlayer().setTimeSinceRest(0);
        event.getPlayer().setSpawn(bedPosition);
        event.getPlayer().teleport(bedPosition, PlayerTeleportEvent.TeleportCause.PLUGIN);
        event.getPlayer().sendMessage("Vyspal jsi se a byl zde nastaven tvůj počáteční bod!");
        instance.getServer().broadcastMessage(
                TextFormat.BLUE + "" + TextFormat.BOLD +
                event.getPlayer().getName() + TextFormat.RESET + " " + TextFormat.WHITE +
                "šel spát a udělal nám den!"
        );
    }

    // Event Holders

    private void rightClickEvent(PlayerInteractEvent event) {
        Item inHand = event.getPlayer().getInventory().getItemInHand();
        if (inHand.isHoe() && inHand.getTier() == 1) { // is Wooden Hoe
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

        if (inHand instanceof ItemCompass) {
            Position pos = event.getPlayer().getPosition().floor();
            int biomeId = event.getPlayer().getLevel().getBiomeId((int) pos.getX(), (int) pos.getZ());
            event.getPlayer().sendTip(
                    TextFormat.GREEN + "X" + TextFormat.GRAY + ": " + TextFormat.WHITE + pos.getX() + TextFormat.GRAY + " | " +
                    TextFormat.GREEN + "Y" + TextFormat.GRAY + ": " + TextFormat.WHITE + pos.getY() + TextFormat.GRAY + " | " +
                    TextFormat.GREEN + "Z" + TextFormat.GRAY + ": " + TextFormat.WHITE + pos.getZ() + TextFormat.GRAY + " | " +
                    TextFormat.GREEN + "Biome" + TextFormat.GRAY + ": " + TextFormat.WHITE + SafariUtils.getBiomeNameById(biomeId)
            );
        }
    }
}
