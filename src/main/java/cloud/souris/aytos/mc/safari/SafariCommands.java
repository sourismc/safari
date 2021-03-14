package cloud.souris.aytos.mc.safari;

import cloud.souris.aytos.mc.safari.npcs.EntityUtils;
import cloud.souris.aytos.mc.safari.npcs.HumanNPC;
import cn.nukkit.Player;
import cn.nukkit.command.Command;

import java.util.concurrent.CompletableFuture;

public class SafariCommands {
    public static void safari(SafariPlugin instance, Player sender, Command command, String label, String[] args) {
        CompletableFuture.runAsync(() -> {
            if (args.length > 0) {
                switch (args[0]) {
                    case "tp":
                        if (args.length != 2) {
                            sender.sendMessage("Použij /safari tp (home|worldspawn)");
                        } else {
                            switch (args[1]) {
                                case "home":
                                    EntityUtils.teleportPlayerToHome(instance, sender);
                                    break;
                                case "worldspawn":
                                    EntityUtils.teleportPlayerToWorldSpawn(instance, sender);
                                    break;
                            }
                        }
                        break;
                    case "sethome":
                        EntityUtils.setPlayersHome(instance, sender);
                        break;
                    default:
                        sender.sendMessage("Použij /safar (tp|sethome) ...");
                        break;
                }
            } else {
                sender.sendMessage("SafariCommand with no args was called");
            }
        });
    }

    public static void npc(SafariPlugin instance, Player sender, Command command, String label, String[] args) {
        CompletableFuture.runAsync(() -> {
            if (args.length < 2 || !args[0].equalsIgnoreCase("spawn")) {
                sender.sendMessage("Zkus /npc spawn [jmeno_npc]");
                return;
            }

            String npcName = args[1].trim();
            if (npcName.isEmpty() || npcName.length() < 3) {
                sender.sendMessage("Zkus /npc spawn [jmeno_npc]");
                return;
            }

            // Compose spaces-friendly name from arguments
            npcName = String.join(" ", args)
                    .replaceFirst("spawn", "")
                    .replaceFirst(" ", "");

            if (instance.npcExists(npcName)) {
                sender.sendMessage("NPC s jmenem " + args[1] + " jiz existuje, zkus jine!");
                return;
            }

            EntityUtils.spawnNPC(instance, sender, HumanNPC.class.getSimpleName(), npcName);
        });
    }
}
