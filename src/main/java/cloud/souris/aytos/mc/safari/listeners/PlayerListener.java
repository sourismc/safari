package cloud.souris.aytos.mc.safari.listeners;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookWritten;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;

import static cloud.souris.aytos.mc.safari.listeners.AreasListener.hoeEvent;

public class PlayerListener implements Listener {
    private final SafariPlugin instance;

    private final String[] introBookLines = {
            "Ahoj! Tady se dozvíš co a jak.\nJe to takový stručný návod k tomuhle serveru!\nOBSAH:\n  3. - 4. strana = Rezidence", // 1
            "", // 2
            "REZIDENCE:\n Pokud si chceš založit rezidenci, potřebuješ dřevěnou motyku a dojít do středu tvé nové rezidence.\n " + // 3
                "Od místa kde stojíš se na každou stranu (150 kostek) ohraničí tvá rezidence, kde budeš ty a tvoje zásoby v bezpečí!",
            " V místě použij pravé tlačítko myši s dřevěnou motykou v ruce a objeví se ti formulář pro založení rezidence.\n" + // 4
                " Úpravný formulář je prozatím ve výstavbě, a pokud jsi v cizí (prozatím i své) rezidenci server ti to napíše do chatu!",
            "", // 5
            "ZÁVĚR:\n Doufáme že se tady budete bavit a hrát tu rádi! :]" // 6
    };

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

        ItemBookWritten safariBook = (ItemBookWritten) Item.get(387, 0, 1);
        safariBook.writeBook(
                TextFormat.RED + "Souris.CLOUD",
                TextFormat.GREEN + "Příručka serveru",
                introBookLines
        );
        boolean thisPlayerAlreadyHaveIntroBook = false;
        for (Item i : event.getPlayer().getInventory().getContents().values()) {
            if (i.equalsExact(safariBook)) {
                thisPlayerAlreadyHaveIntroBook = true;
                break;
            }
        }
        if (event.getPlayer().getInventory().canAddItem(safariBook) && !thisPlayerAlreadyHaveIntroBook) {
            event.getPlayer().getInventory().addItem(safariBook);
        }

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

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        Vector3 bedPosition = event.getBed().getLocation().floor().asVector3f().asVector3();
        event.getBed().getLevel().setTime(Level.TIME_DAY);
        event.setCancelled(true);
        event.getPlayer().setTimeSinceRest(0);
        event.getPlayer().setSpawn(bedPosition);
        event.getPlayer().teleport(bedPosition, PlayerTeleportEvent.TeleportCause.PLUGIN);
        instance.getServer().broadcastMessage(
                TextFormat.BLUE + "" + TextFormat.BOLD +
                event.getPlayer().getName() + TextFormat.RESET + " " + TextFormat.WHITE +
                "šel spát a udělal nám den!"
        );
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
