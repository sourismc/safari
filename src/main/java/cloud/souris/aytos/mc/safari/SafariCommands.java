package cloud.souris.aytos.mc.safari;

import cn.nukkit.Player;
import cn.nukkit.command.Command;

import java.util.concurrent.CompletableFuture;

public class SafariCommands {
    public static void safari(Player sender, Command command, String label, String[] args) {
        CompletableFuture.runAsync(() -> {
            if (args.length > 0) {
                sender.sendChat("Mame argumenty!");
                sender.sendChat(args[0]);
            }
            sender.sendChat("Ty vol e!");
        });
    }
}
