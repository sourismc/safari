package cloud.souris.aytos.mc.safari;

import cn.nukkit.Player;
import de.lucgameshd.scoreboard.api.ScoreboardAPI;
import de.lucgameshd.scoreboard.network.DisplaySlot;
import de.lucgameshd.scoreboard.network.Scoreboard;
import de.lucgameshd.scoreboard.network.ScoreboardDisplay;

import java.util.Map;
import java.util.UUID;

public class SafariScoreboardUpdater extends Thread {
    private SafariPlugin instance;

    SafariScoreboardUpdater(SafariPlugin instance) {
        this.instance = instance;

        setName("SafariScoreboardUpdater");
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
                    ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "safariSidebar", "\u00a72   SourisMC\u00a77 - \u00a76Safari\u00a77 [\u00a7fsurvival\u00a77]   ");
                    ScoreboardDisplay scoreboardList = scoreboard.addDisplay(DisplaySlot.LIST, "safariList", "SourisMC Bedrock Edition");

                    for (SafariPlayer sxp : instance.players.values()) {
                        Player xp = instance.getServer().getPlayer(sp.getUuid()).get();
                        scoreboardList.addLine(xp.getName(), xp.getPing());
                        int expLevel = xp.getExperienceLevel();
                        StringBuilder levelSB = new StringBuilder();
                        if (expLevel < 12) {
                            levelSB.append("\u00a75");
                        } else if (expLevel < 23) {
                            levelSB.append("\u00a79");
                        } else {
                            levelSB.append("\u00a73");
                        }
                        levelSB.append(expLevel);
                        scoreboardDisplay.addLine(sxp.getSidebarName().replace("<L>", levelSB.toString()), sxp.getMoney());
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
