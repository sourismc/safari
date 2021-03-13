package cloud.souris.aytos.mc.safari.areas;

import cn.nukkit.math.Vector3;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Area {
    public enum AreaType {
        PlayerCreated,
        AdminCreated,
        World
    }

    private String name;
    private UUID ownerId;
    private AreaBounds bounds;
    private AreaType type;
    private HashMap<String, AreaFlag> flags;

    public Area(String name, UUID ownerId, Vector3 posCenter, AreaType type, HashMap<String, AreaFlag> flags) {
        this.name = name;
        this.ownerId = ownerId;
        this.bounds = new AreaBounds(posCenter);
        this.type = type;
        this.flags = flags;
    }

    public Area(Document document) {
        this.name = document.getString("name");
        this.ownerId = UUID.fromString(document.getString("ownerId"));
        Vector3 posCenter = new Vector3();
        posCenter.x = document.getDouble("center_x");
        posCenter.y = document.getDouble("center_y");
        posCenter.z = document.getDouble("center_z");
        this.bounds = new AreaBounds(posCenter);
        this.type = typeFromString(document.getString("type"));
        List<Document> flags = document.getList("flags", Document.class);
        this.flags = new HashMap<>();
        for (Document flag : flags) {
            String key = flag.getString("name");
            boolean value = flag.getBoolean("value");
            this.flags.put(key, new AreaFlag(key, value));
        }
    }

    public Document toDocument() {
        ArrayList<Document> flagsList = new ArrayList<>();
        for (AreaFlag af : flags.values()) {
            flagsList.add(new Document().append("name", af.getName()).append("value", af.isAllowed()));
        }
        return new Document()
                .append("name", name)
                .append("ownerId", ownerId.toString())
                .append("center_x", bounds.getCenter().x)
                .append("center_y", bounds.getCenter().y)
                .append("center_z", bounds.getCenter().z)
                .append("type", typeToString(type))
                .append("flags", flagsList.toArray());
    }

    public Document toDocumentDescriptor() {
        return new Document()
                .append("name", name)
                .append("ownerId", ownerId.toString());
    }

    public boolean isAllowed(String flagName) {
        if (!flags.containsKey(flagName)) {
            return false;
        }
        return flags.get(flagName).isAllowed();
    }

    public void setAllowed(String flagName, boolean allowed) {
        if (flags.containsKey(flagName)) {
            flags.get(flagName).setAllowed(allowed);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public AreaBounds getBounds() {
        return bounds;
    }

    public void setBounds(AreaBounds bounds) {
        this.bounds = bounds;
    }

    public AreaType getType() {
        return type;
    }

    public void setType(AreaType type) {
        this.type = type;
    }

    public HashMap<String, AreaFlag> getFlags() {
        return flags;
    }

    public void setFlags(HashMap<String, AreaFlag> flags) {
        this.flags = flags;
    }

    public String typeToString(AreaType type) {
        switch (type) {
            case AdminCreated: return "admin";
            case PlayerCreated: return "player";
            case World: return "world";
        }

        return "undefined";
    }

    public AreaType typeFromString(String type) {
        switch (type) {
            case "admin": return AreaType.AdminCreated;
            case "player": return AreaType.PlayerCreated;
            case "world": return AreaType.World;
        }

        return null;
    }
}
