package cloud.souris.aytos.mc.safari.updaters;

import cloud.souris.aytos.mc.safari.SafariPlayer;
import cloud.souris.aytos.mc.safari.SafariPlugin;
import cloud.souris.aytos.mc.safari.SafariUtils;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import de.lucgameshd.scoreboard.api.ScoreboardAPI;
import de.lucgameshd.scoreboard.network.DisplaySlot;
import de.lucgameshd.scoreboard.network.Scoreboard;
import de.lucgameshd.scoreboard.network.ScoreboardDisplay;

public class ScoreboardUpdater extends Thread {
    private final SafariPlugin instance;

    public ScoreboardUpdater(SafariPlugin instance) {
        this.instance = instance;

        setName("ScoreboardUpdater");
    }

    @Override
    public void run() {
        try {
            if (!instance.players.isEmpty()) {
                for (SafariPlayer sp : instance.players.values()) {
                    Player p = instance.getServer().getPlayer(sp.getUuid()).get();
                    if (!p.spawned) {
                        continue;
                    }

                    Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
                    String sidebarTitle = "         " + TextFormat.GREEN + "Souris" + TextFormat.MINECOIN_GOLD + "MC" +
                        TextFormat.WHITE + " - " + TextFormat.GRAY + SafariUtils.formatMinecraftWorldTime(p.getLevel().getTime()) + "         ";
                    ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "safariSidebar", sidebarTitle);
//                    ScoreboardDisplay scoreboardList = scoreboard.addDisplay(DisplaySlot.LIST, "safariList", "SourisMC Bedrock Edition");

                    for (SafariPlayer sxp : instance.players.values()) {
                        Player xp = instance.getServer().getPlayer(sp.getUuid()).get();
//                        scoreboardList.addLine(xp.getName(), xp.getPing());
                        scoreboardDisplay.addLine(sxp.getSidebarNameWithPlayer(xp), xp.getPing());
                    }

                    try {
                        instance.scoreboards.get(p.getUniqueId()).hideFor(p);
                    } catch (Exception ignored) {}

                    scoreboard.showFor(p);
                    instance.scoreboards.put(p.getUniqueId(), scoreboard);
                }
            }
        } catch (Exception ignored) {}
    }
}
