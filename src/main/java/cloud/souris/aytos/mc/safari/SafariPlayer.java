package cloud.souris.aytos.mc.safari;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import org.bson.Document;

import java.util.Objects;
import java.util.UUID;

public class SafariPlayer {
    private UUID uuid;
    private String name;
    private int money;
    private String group;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public SafariPlayer(UUID uuid, String name, int money, String group) {
        this.uuid = uuid;
        this.name = name;
        this.money = money;
        this.group = group;
    }

    @Override
    public String toString() {
        return "SafariPlayer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", money=" + money +
                ", group='" + group + '\'' +
                '}';
    }

    public String getListName() {
        StringBuilder sb = new StringBuilder();
        switch (this.group) {
            default:
            case "player":
                sb.append("\u00a7r");
                break;
            case "staff":
                sb.append("\u00a76");
                break;
        }

        return sb.toString();
    }

    public String getSidebarName() {
        StringBuilder sb = new StringBuilder();
        sb.append(TextFormat.DARK_GRAY);
        sb.append("[");
        TextFormat nameColor;
        switch (this.group) {
            default:
            case "player":
                sb.append(TextFormat.WHITE);
                sb.append("LVL-<L>");
                nameColor = TextFormat.GRAY;
                break;
            case "staff":
                sb.append(TextFormat.GREEN);
                sb.append("SourisMC");
                nameColor = TextFormat.GOLD;
                break;
        }
        sb.append(TextFormat.DARK_GRAY);
        sb.append("] ");
        sb.append(nameColor);
        sb.append(this.name);

        return sb.toString();
    }

    public String getSidebarNameWithPlayer(Player player) {
        return getSidebarName().replace("<L>", formatPlayerXPLevel(player.getExperienceLevel()));
    }

    public String getScoreTagWithPlayer(Player player) {
        return "" + TextFormat.UNDERLINE + TextFormat.GOLD + "BOSS";
    }

    public static String formatPlayerXPLevel(int expLevel) {
        StringBuilder levelSB = new StringBuilder();
        if (expLevel < 12) {
            levelSB.append(TextFormat.DARK_BLUE);
        } else if (expLevel < 23) {
            levelSB.append(TextFormat.BLUE);
        } else {
            levelSB.append(TextFormat.AQUA);
        }
        levelSB.append(expLevel);

        return levelSB.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SafariPlayer that = (SafariPlayer) o;
        return getMoney() == that.getMoney() && getUuid() == that.getUuid() && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getName(), getMoney());
    }

    public static SafariPlayer fromDocument(Document document) {
        return new SafariPlayer(UUID.fromString(document.getString("uuid")), document.getString("name"), document.getInteger("money"), document.getString("group"));
    }
}