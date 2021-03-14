package cloud.souris.aytos.mc.safari.updaters;

import cloud.souris.aytos.mc.safari.SafariPlayer;
import cloud.souris.aytos.mc.safari.SafariPlugin;
import cn.nukkit.Player;

public class NameTagUpdater extends Thread {
    private final SafariPlugin instance;

    public NameTagUpdater(SafariPlugin instance) {
        this.instance = instance;

        setName("NameTagUpdater");
    }

    @Override
    public void run() {
        try {
            for (SafariPlayer sp : instance.players.values()) {
                Player player = instance.getServer().getPlayer(sp.getUuid()).get();
                String nameTag = sp.getSidebarNameWithPlayer(player);
                String scoreTag = sp.getScoreTagWithPlayer(player);
                if (!player.getNameTag().equalsIgnoreCase(nameTag)) {
                    player.setNameTag(nameTag);
                }
                if (!player.getScoreTag().equalsIgnoreCase(scoreTag)) {
                    player.setScoreTag(nameTag);
                }
            }
        } catch (Exception ignored) {}
    }
}
