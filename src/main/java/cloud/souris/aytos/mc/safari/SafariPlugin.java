package cloud.souris.aytos.mc.safari;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;

public class SafariPlugin extends PluginBase implements Listener {
    private static SafariPlugin instance;

    public SafariDataProvider dataProvider;
    private HashMap<Long, SafariPlayer> players;

    @Override
    public void onLoad() {
        getLogger().info(TextFormat.WHITE + "Loaded in memory");
    }

    @Override
    public void onEnable() {
        getLogger().info(TextFormat.DARK_GREEN + "Enabled");

        getServer().getPluginManager().registerEvents(this, this);

        instance = this;
        dataProvider = new SafariDataProvider();
        dataProvider.connect(this);
        players = new HashMap<>();
    }

    @Override
    public void onDisable() {
        dataProvider.disconnect(this);
        getLogger().info(TextFormat.DARK_RED + "Disabled");
    }

    public static SafariPlugin getInstance() {
        return instance;
    }

    public void addPlayer(long uuid, SafariPlayer safariPlayer) {
        this.players.put(uuid, safariPlayer);
    }

    public void getPlayer(long uuid) {
        this.players.get(uuid);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("No nazdaar, " + event.getPlayer().getName());
        getLogger().info(TextFormat.WHITE + "Takze " + event.getPlayer().getName() + " je " + event.getPlayer().getUniqueId());

        dataProvider.initializePlayer(this, event.getPlayer());
    }
}
