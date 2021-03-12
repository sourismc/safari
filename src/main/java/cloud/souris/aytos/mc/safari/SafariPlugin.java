package cloud.souris.aytos.mc.safari;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import de.lucgameshd.scoreboard.network.Scoreboard;
import ru.nukkitx.forms.elements.CustomForm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class SafariPlugin extends PluginBase implements Listener {
    private static SafariPlugin instance;

    public SafariDataProvider dataProvider;
    public HashMap<UUID, SafariPlayer> players;
    public HashMap<UUID, Scoreboard> scoreboards;
    public HashMap<UUID, Long> hoeCooldowns;

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
        hoeCooldowns = new HashMap<>();

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

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            if (event.getPlayer().getInventory().getItemInHand().isHoe()) {
                Long now = System.currentTimeMillis();
                if (hoeCooldowns.containsKey(event.getPlayer().getUniqueId())) {
                    getLogger().info("We have cooldown, so we check how long it is");
                    Long cd = hoeCooldowns.get(event.getPlayer().getUniqueId());
                    long diff = now - cd;
                    event.getPlayer().sendChat("Ted je: " + now + " ; a bylo: " + cd + " ; a to znamena => " + diff);
                } else {
                    hoeCooldowns.put(event.getPlayer().getUniqueId(), now);
                }
                event.getPlayer().sendChat("Ty zmrde!!!!!!!!");
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("safari") && sender instanceof Player) {
            SafariCommands.safari((Player) sender, command, label, args);

//            CustomForm customForm = new CustomForm()
//                    .addLabel("Nazdar, takze takhle to udelame")
//                    .addDropDown("Akce", Arrays.asList("Test 1", "Test 2", "Spawnout NPC"))
//                    .addInput("Pridavny argumenty")
//                    .addSlider("Prodleva", 0, 100)
//                    .addToggle("Zalogovat", false);
//
//            customForm.send(p, (targetPlayer, targetForm, data) -> {
//                if (data == null) {
//                    targetPlayer.sendChat("Ty zmrde!");
//                    return;
//                }
//
//                targetPlayer.sendMessage(data.toString());
//            });
        }

        return true;
    }
}
