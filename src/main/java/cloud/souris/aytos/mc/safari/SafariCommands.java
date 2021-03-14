package cloud.souris.aytos.mc.safari;

import cloud.souris.aytos.mc.safari.npcs.EntityUtils;
import cloud.souris.aytos.mc.safari.npcs.HumanNPC;
import cn.nukkit.Player;
import cn.nukkit.command.Command;

import java.util.concurrent.CompletableFuture;

public class SafariCommands {
    public static void safari(Player sender, Command command, String label, String[] args) {
        CompletableFuture.runAsync(() -> {
            if (args.length > 0) {
                sender.sendChat("Mame argumenty, co ale s nima?");
                sender.sendChat(args[0]);
            }
            sender.sendChat("Ty vole, to je psycho!");
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
