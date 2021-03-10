package cloud.souris.aytos.mc.safari;

import org.bson.Document;

import java.util.Objects;

public class SafariPlayer {
    private long uuid;
    private String name;
    private int money;

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
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

    public SafariPlayer(long uuid, String name, int money) {
        this.uuid = uuid;
        this.name = name;
        this.money = money;
    }

    @Override
    public String toString() {
        return "SafariPlayer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
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
        return new SafariPlayer(document.getLong("uuid"), document.getString("name"), document.getInteger("money"));
    }
}