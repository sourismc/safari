package cloud.souris.aytos.mc.safari;

import cloud.souris.aytos.mc.safari.areas.Area;
import cn.nukkit.Player;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SafariDataProvider {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> playersData;
    private MongoCollection<Document> areas;

    public void connect(SafariPlugin instance) {
        CompletableFuture.runAsync(() -> {
            MongoClientURI uri = new MongoClientURI("mongodb://nukkit:nukkit@localhost:33277/?authSource=nukkit");
            this.mongoClient = new MongoClient(uri);
            this.mongoDatabase = this.mongoClient.getDatabase("nukkit");
            this.playersData = this.mongoDatabase.getCollection("players");
            this.areas = this.mongoDatabase.getCollection("areas");

            instance.getLogger().info("MongoDB Connected");
            loadAreasAsync(instance);
        });
    }

    public void disconnect(SafariPlugin instance) {
        this.mongoClient.close();
        instance.getLogger().info("MongoDB Disconnected");
    }

    public void initializePlayer(SafariPlugin instance, Player player) {
        CompletableFuture.runAsync(() -> {
            Document existing = this.playersData.find(new Document("uuid", player.getUniqueId().toString())).first();
            SafariPlayer safariPlayer;
            if (existing == null) {
                Document document = new Document("uuid", player.getUniqueId().toString())
                        .append("name", player.getName())
                        .append("money", 100)
                        .append("group", "player");
                this.playersData.insertOne(document);
                safariPlayer = SafariPlayer.fromDocument(document);
                instance.getLogger().info("PlayerInfo created :)");
            } else {
                safariPlayer = SafariPlayer.fromDocument(existing);
                instance.getLogger().info("PlayerInfo fetched :)");
            }

            instance.addPlayer(player.getUniqueId(), safariPlayer);
            player.setNameTagAlwaysVisible(true);
            player.setNameTag(safariPlayer.getSidebarName());
            setPlayerOnlineStatus(instance, player.getUniqueId(), true);
        });
    }

    public void setPlayerOnlineStatusAsync(SafariPlugin instance, UUID uuid, boolean onlineStatus) {
        CompletableFuture.runAsync(() -> {
            setPlayerOnlineStatus(instance, uuid, onlineStatus);
        });
    }

    public void setPlayerOnlineStatus(SafariPlugin instance, UUID uuid, boolean onlineStatus) {
        try {
            playersData.updateOne(Filters.eq("uuid", uuid.toString()), new Document("onlineStatus", onlineStatus));
        } catch (Exception ignored) {
            instance.getLogger().error("some error", ignored);
            instance.getLogger().info("User for update online status was not found!");
        }
    }

    public void loadAreasAsync(SafariPlugin instance) {
        CompletableFuture.runAsync(() -> {
            loadAreas(instance);
        });
    }

    public void loadAreas(SafariPlugin instance) {
        instance.getLogger().info("Loading areas...");
        instance.areas.clear();
        for (Document document : this.areas.find(new Document())) {
            instance.areas.add(new Area(document));
            instance.getLogger().info("Adding area " + document.getString("name"));
        }
    }

    public void saveSingleArea(SafariPlugin instance, Area area) {
        if (isAreaInDatabase(area)) {
            areas.updateOne(Filters.and(Filters.eq("name", area.getName()), Filters.eq("ownerId", area.getOwnerId().toString())), area.toDocument());
            instance.getLogger().info("Area saved in db! " + area.getName());
        } else {
            areas.insertOne(area.toDocument());
            instance.getLogger().info("Area updated in db! " + area.getName());
        }
    }

    public void saveSingleAreaAsync(SafariPlugin instance, Area area) {
        CompletableFuture.runAsync(() -> {
            saveSingleArea(instance, area);
        });
    }

    public void saveAreasSync(SafariPlugin instance) {
        for (Area area : instance.areas) {
            saveSingleArea(instance, area);
        }
    }

    public void saveAreasAsync(SafariPlugin instance) {
        CompletableFuture.runAsync(() -> {
            saveAreasSync(instance);
        });
    }

    private boolean isAreaInDatabase(Area a) {
        Document existing = this.areas.find(a.toDocumentDescriptor()).first();
        return existing != null;
    }
}