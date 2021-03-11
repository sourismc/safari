package cloud.souris.aytos.mc.safari;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import de.lucgameshd.scoreboard.network.Scoreboard;

import java.util.HashMap;
import java.util.UUID;

public class SafariPlugin extends PluginBase implements Listener {
    private static SafariPlugin instance;

    public SafariDataProvider dataProvider;
    public HashMap<UUID, SafariPlayer> players;
    public HashMap<UUID, Scoreboard> scoreboards;

    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "Loaded in memory");
    }

    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "Enabled");

        getServer().getPluginManager().registerEvents(this, this);

        instance = this;
        dataProvider = new SafariDataProvider();
        dataProvider.connect(this);
        players = new HashMap<>();
        scoreboards = new HashMap<>();

        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new SafariScoreboardUpdater(this), 80, 80, true);
    }

    @Override
    public void onDisable() {
        dataProvider.disconnect(this);
        getLogger().info(TextFormat.DARK_RED + "Disabled");
    }

    public static SafariPlugin getInstance() {
        return instance;
    }

    public void addPlayer(UUID uuid, SafariPlayer safariPlayer) {
        this.players.put(uuid, safariPlayer);
    }

    public void getPlayer(UUID uuid) {
        this.players.get(uuid);
    }

    public void removePlayer(UUID uuid) { this.players.remove(uuid); }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("No nazdaar, " + event.getPlayer().getName());
        getLogger().info(TextFormat.WHITE + "Takze " + event.getPlayer().getName() + " je " + event.getPlayer().getUniqueId().toString());

        event.getPlayer().setNameTag("\u00a7f] \u00a76" + event.getPlayer().getName());
        event.getPlayer().setNameTagAlwaysVisible(true);

        dataProvider.initializePlayer(this, event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("Tak cuus no, " + event.getPlayer().getName());
        removePlayer(event.getPlayer().getUniqueId());

        getLogger().info(TextFormat.WHITE + "Okay, odpojil se " + event.getPlayer().getName() + " a ma teda " + event.getPlayer().getUniqueId().toString());
    }
}
