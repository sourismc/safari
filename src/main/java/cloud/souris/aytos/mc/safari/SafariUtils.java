package cloud.souris.aytos.mc.safari;

import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookWritten;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class SafariUtils {
    public static String formatMinecraftWorldTime(int levelTime) {
        int minutes = (levelTime % 1000) * 60 / 1000;
        int hours = (levelTime / 1000) + 6;
        if (hours == 24) {
            hours = 0;
        }

        return String.format("%02d:%02d", hours, minutes); // 4609:22   ????
    }

    public static void givePlayerIntroBook(PlayerInventory inventory) {
        ItemBookWritten safariBook = (ItemBookWritten) Item.get(387, 0, 1);
        safariBook.writeBook(
                TextFormat.RED + "Souris.CLOUD",
                TextFormat.GREEN + "Příručka serveru",
                introBookPages
        );
        boolean thisPlayerAlreadyHaveIntroBook = false;
        for (Item i : inventory.getContents().values()) {
            if (i.equalsExact(safariBook)) {
                thisPlayerAlreadyHaveIntroBook = true;
                break;
            }
        }
        if (inventory.canAddItem(safariBook) && !thisPlayerAlreadyHaveIntroBook) {
            inventory.addItem(safariBook);
        }
    }

    public static void givePlayerIntroBookAsync(PlayerInventory inventory) {
        CompletableFuture.runAsync(() -> {
            givePlayerIntroBook(inventory);
        });
    }

    public static String getBiomeNameById(int biomeId) {
        return biomeStrings.getOrDefault(biomeId, "Unknown");
    }

    // RESOURCES

    public static String[] introBookPages = { // 32 znaku na radek | max 50 stran
            " Ahoj! Tady se dozvíš co a jak,\nje to takový návod k tomuhle serveru!", // 1
            "OBSAH:\n  3. strana = Rezidence\n  4. strana = Příkazy", // 2
            "REZIDENCE:\n Pokud si chceš založit rezidenci, potřebuješ dřevěnou motyku a dojít do středu tvé nové rezidence.\n" + // 3
                    " Od místa kde stojíš se na každou stranu (150 kostek) ohraničí tvá rezidence, kde budeš ty a tvoje zásoby v bezpečí!\n" +
                    " V místě použij pravé tlačítko myši s dřevěnou motykou v ruce a objeví se ti formulář pro založení rezidence.\n" +
                    " Úpravný formulář je prozatím ve výstavbě, a pokud jsi v cizí (prozatím i své) rezidenci server ti to napíše do chatu!",
            "PŘÍKAZY:\n* /safari tp home - teleportuje tě tvůj spawn\n* /safari tp worldspawn - teleportuje tě na world spawn", // 4
            "", // 5
            "ZÁVĚR:\n Doufáme že se tady budete bavit a hrát tu rádi! :]" // 6
    };

    public static HashMap<Integer, String> biomeStrings = new HashMap<Integer, String>() {
        {
            put(42, "Ocean"); put(43, "Legacy Frozen Ocean"); put(24, "Deep Ocean"); put(10, "Frozen Ocean");
            put(50, "Deep Frozen Ocean"); put(46, "Cold Ocean"); put(49, "Deep Cold Ocean"); put(45, "Lukewarm Ocean");
            put(48, "Deep Lukewarm Ocean"); put(44, "Warm Ocean"); put(47, "Deep Warm Ocean"); put(7, "River");
            put(11, "Frozen River"); put(16, "Beach"); put(25, "Stone Shore"); put(26, "Snowy Beach");
            put(4, "Forest"); put(18, "Wooded Hills"); put(132, "Flower Forest"); put(27, "Birch Forest");
            put(28, "Birch Forest Hills"); put(155, "Tall Birch Forest"); put(156, "Tall Birch Hills"); put(29, "Dark Forest");
            put(157, "Dark Forest Hills"); put(21, "Jungle"); put(22, "Jungle Hills"); put(149, "Modified Jungle");
            put(23, "Jungle Edge"); put(151, "Modified Jungle Edge"); put(168, "Bamboo Jungle"); put(169, "Bamboo Jungle Hills");
            put(5, "Taiga");  put(19, "Taiga Hills"); put(133, "Taiga Mountains"); put(30, "Snowy Taiga");
            put(31, "Snowy Taiga Hills"); put(158, "Snowy Taiga Mountains"); put(32, "Giant Tree Taiga"); put(33, "Giant Tree Taiga Hills");
            put(160, "Giant Spruce Taiga"); put(161, "Giant Spruce Taiga Hills"); put(14, "Mushroom Fields"); put(15, "Mushroom Field Shore");
            put(6, "Swamp"); put(134, "Swamp Hills"); put(35, "Savanna"); put(36, "Savanna Plateau"); put(163, "Shattered Savanna");
            put(164, "Shattered Savanna Plateau"); put(1, "Plains"); put(129, "Sunflower Plains"); put(2, "Desert");
            put(17, "Desert Hills"); put(130, "Desert Lakes"); put(12, "Snowy Tundra"); put(13, "Snowy Mountains");
            put(140, "Ice Spikes"); put(3, "Mountains"); put(34, "Wooded Mountains"); put(131, "Gravelly Mountains");
            put(162, "Gravelly Mountains+"); put(20, "Mountain Edge"); put(37, "Badlands"); put(39, "Badlands Plateau");
            put(167, "Modified Badlands Plateau"); put(38, "Wooded Badlands Plateau"); put(166, "Modified Wooded Badlands Plateau");
            put(165, "Eroded Badlands"); put(8, "Nether Wastes"); put(179, "Crimson Forest"); put(180, "Warped Forest");
            put(178, "Soul Sand Valley"); put(181, "Basalt Deltas"); put(9, "The End");
        }
    };
}
