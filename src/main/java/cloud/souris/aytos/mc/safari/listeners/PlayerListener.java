package cloud.souris.aytos.mc.safari.listeners;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cloud.souris.aytos.mc.safari.areas.Area;
import cloud.souris.aytos.mc.safari.areas.AreaBounds;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import ru.nukkitx.forms.elements.CustomForm;

public class PlayerListener implements Listener {
    private final SafariPlugin instance;

    public PlayerListener(SafariPlugin instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("No nazdaar, " + event.getPlayer().getName());
        instance.getLogger().info(TextFormat.WHITE + "Takze " + event.getPlayer().getName() + " je " + event.getPlayer().getUniqueId().toString());

        event.getPlayer().setNameTag("\u00a7f] \u00a76" + event.getPlayer().getName());
        event.getPlayer().setNameTagAlwaysVisible(true);

        instance.dataProvider.initializePlayer(instance, event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("Tak cuus no, " + event.getPlayer().getName());
        instance.removePlayer(event.getPlayer().getUniqueId());

        instance.getLogger().info(TextFormat.WHITE + "Okay, odpojil se " + event.getPlayer().getName() + " a ma teda " + event.getPlayer().getUniqueId().toString());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            rightClickEvent(event);
        }
    }

    private void rightClickEvent(PlayerInteractEvent event) {
        Item inHand = event.getPlayer().getInventory().getItemInHand();
        if (inHand.isHoe() && inHand.getTier() == 1) { // Is wooden hoe
            Long now = System.currentTimeMillis();
            if (instance.hoeCooldowns.containsKey(event.getPlayer().getUniqueId())) {
                Long cd = instance.hoeCooldowns.get(event.getPlayer().getUniqueId());
                long diff = now - cd;
                instance.hoeCooldowns.put(event.getPlayer().getUniqueId(), now);
                if (diff > 100) {
                    hoeEvent(event);
                }
            } else {
                instance.hoeCooldowns.put(event.getPlayer().getUniqueId(), now);
                hoeEvent(event);
            }
        }
    }

    private void hoeEvent(PlayerInteractEvent event) {
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
//                    .addLabel("Nazdar, takze takhle to udelame")
//                    .addDropDown("Akce", Arrays.asList("Test 1", "Test 2", "Spawnout NPC"))
//                    .addInput("Pridavny argumenty")
//                    .addSlider("Prodleva", 0, 100)
//                    .addToggle("Zalogovat", false);
    }
}
