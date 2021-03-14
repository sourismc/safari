package cloud.souris.aytos.mc.safari;

import cloud.souris.aytos.mc.safari.npcs.EntityUtils;
import cloud.souris.aytos.mc.safari.npcs.HumanNPC;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;

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
            if (args.length < 2 || (!args[0].equalsIgnoreCase("spawn") && !args[0].equalsIgnoreCase("despawn"))) {
                sender.sendMessage("Zkus /npc (spawn|despawn) args...");
                return;
            }

            switch (args[0]) {
                case "spawn":
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
                    // Spawn NPC
                    EntityUtils.spawnNPC(instance, sender, HumanNPC.class.getSimpleName(), npcName);
                    break;
                case "despawn":
                    try {
                        int npcId = Integer.getInteger(args[1]);
                        instance.getLogger().info("argument int: " + npcId);
                        Entity entity = sender.getLevel().getEntity(npcId);
                        instance.getLogger().info("checking null");
                        if (entity != null) {
                            instance.getLogger().info("entity found");
                            entity.close();
                            sender.sendMessage("NPC bylo smazano!");
                        } else {
                            sender.sendMessage("Zadne NPC s ID " + args[1] + " neexistuje!");
                        }
                    } catch (Exception exception) {
                        sender.sendMessage("Zkus /npc despawn [id_npc]");
                        instance.getLogger().error("NPC DESPAWN", exception);
                    }
                    break;
                default:
                    sender.sendMessage("Zkus /npc (spawn|despawn) args...");
                    return;
            }
        });
    }
}
