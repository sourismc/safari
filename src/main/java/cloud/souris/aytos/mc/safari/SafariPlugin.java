package cloud.souris.aytos.mc.safari;

import cloud.souris.aytos.mc.safari.areas.Area;
import cloud.souris.aytos.mc.safari.areas.AreaFlag;
import cloud.souris.aytos.mc.safari.listeners.AreasListener;
import cloud.souris.aytos.mc.safari.listeners.NPCListener;
import cloud.souris.aytos.mc.safari.listeners.PlayerListener;
import cloud.souris.aytos.mc.safari.npcs.HumanNPC;
import cloud.souris.aytos.mc.safari.updaters.NameTagUpdater;
import cloud.souris.aytos.mc.safari.updaters.ScoreboardUpdater;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Listener;
import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import de.lucgameshd.scoreboard.network.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class SafariPlugin extends PluginBase implements Listener {
    private static SafariPlugin instance;

    public SafariDataProvider dataProvider;
    public HashMap<UUID, SafariPlayer> players;
    public HashMap<UUID, Scoreboard> scoreboards;
    public HashMap<UUID, Long> hoeCooldowns;
    public HashMap<UUID, Long> areaMessageCooldowns;
    public ArrayList<Area> areas;
    public HashMap<String, Entity> npcs;

    @Override
    public void onLoad() {
        instance = this;
        this.getLogger().info(TextFormat.WHITE + "Loaded in memory");
    }

    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "Enabled");
        this.getServer().getNetwork().setName(TextFormat.GREEN + "Souris" + TextFormat.MINECOIN_GOLD + "MC"); // set MOTD
        initialize();
    }

    @Override
    public void onDisable() {
        dispose();
        getLogger().info(TextFormat.DARK_RED + "Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // na tohle jsem fakt pysnej, na to ze Javu neznam :D
        Function<String, Boolean> commandCompare = (compare) -> command.getName().equalsIgnoreCase(compare) && sender instanceof Player;

        if (commandCompare.apply("safari")) {
            SafariCommands.safari((Player) sender, command, label, args);
        }

        if (commandCompare.apply("npc")) {
            SafariCommands.npc(this, (Player) sender, command, label, args);
        }

        if (commandCompare.apply("xtime")) {
            Player p = (Player) sender;
            p.sendMessage("Aktuální čas je " + p.getLevel().getTime());
        }

        return true;
    }

    private void registerEventListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new AreasListener(this), this);
        getServer().getPluginManager().registerEvents(new NPCListener(this), this);
    }

    private void registerNPCs() {
        Entity.registerEntity(HumanNPC.class.getSimpleName(), HumanNPC.class);
    }

    private void initialize() {
        registerEventListeners();
        registerNPCs();

        dataProvider = new SafariDataProvider();
        dataProvider.connect(this);
        players = new HashMap<>();
        scoreboards = new HashMap<>();
        hoeCooldowns = new HashMap<>();
        areaMessageCooldowns = new HashMap<>();
        areas = new ArrayList<>();
        npcs = new HashMap<>();

        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new ScoreboardUpdater(this), 80, 80, true);
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new NameTagUpdater(this), 100, 100, true);
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

    public void addNPC(String name, Entity entity) { this.npcs.put(name, entity); }

    public Entity getNPC(String name) { return this.npcs.get(name); }

    public void removeNPC(String name) { this.npcs.remove(name); }

    public boolean npcExists(String name) { return this.npcs.containsKey(name); }

    public Area getAreaByPosition(Position position) {
        for (Area area : areas) {
            if (area.getBounds().checkCollision(position)) {
                return area;
            }
        }
        return null;
    }

    public void createArea(String areaName, Player owner) {
        AreaFlag disableMobs = new AreaFlag("disableMobs", true);
        HashMap<String, AreaFlag> flags = new HashMap<>();
        flags.put(disableMobs.getName(), disableMobs);
        Area area = new Area(areaName, owner.getUniqueId(), owner.getPosition().asVector3f().asVector3().floor(), Area.AreaType.PlayerCreated, flags);

        areas.add(area);
        dataProvider.saveSingleAreaAsync(this, area);
    }
}