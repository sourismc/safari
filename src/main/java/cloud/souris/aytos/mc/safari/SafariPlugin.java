package cloud.souris.aytos.mc.safari;

import cloud.souris.aytos.mc.safari.areas.Area;
import cloud.souris.aytos.mc.safari.listeners.PlayerListener;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import de.lucgameshd.scoreboard.network.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SafariPlugin extends PluginBase implements Listener {
    private static SafariPlugin instance;

    public SafariDataProvider dataProvider;
    public HashMap<UUID, SafariPlayer> players;
    public HashMap<UUID, Scoreboard> scoreboards;
    public HashMap<UUID, Long> hoeCooldowns;
    public ArrayList<Area> areas;

    @Override
    public void onLoad() {
        instance = this;
        this.getLogger().info(TextFormat.WHITE + "Loaded in memory");
    }

    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "Enabled");
        initialize();
    }

    @Override
    public void onDisable() {
        dispose();
        getLogger().info(TextFormat.DARK_RED + "Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("safari") && sender instanceof Player) {
            SafariCommands.safari((Player) sender, command, label, args);
        }

        return true;
    }

    private void initialize() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        dataProvider = new SafariDataProvider();
        dataProvider.connect(this);
        players = new HashMap<>();
        scoreboards = new HashMap<>();
        hoeCooldowns = new HashMap<>();
        areas = new ArrayList<>();

        dataProvider.loadAreasAsync(this);

        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new SafariScoreboardUpdater(this), 80, 80, true);
    }

    private void dispose() {
        dataProvider.saveAreasSync(this);
        dataProvider.disconnect(this);
    }

    public static SafariPlugin getInstance() {
        return instance;
    }

    public void addPlayer(UUID uuid, SafariPlayer safariPlayer) {
        this.players.put(uuid, safariPlayer);
    }

    public SafariPlayer getPlayer(UUID uuid) {
        return this.players.get(uuid);
    }

    public void removePlayer(UUID uuid) { this.players.remove(uuid); }
}
