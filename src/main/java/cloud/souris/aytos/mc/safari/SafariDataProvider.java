package cloud.souris.aytos.mc.safari;

import cn.nukkit.player.Player;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.concurrent.CompletableFuture;

public class SafariDataProvider {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> playersData;

    public void connect(SafariPlugin instance) {
        CompletableFuture.runAsync(() -> {
            MongoClientURI uri = new MongoClientURI("mongodb://nukkit:nukkit@localhost:33277/?authSource=nukkit");
            this.mongoClient = new MongoClient(uri);
            this.mongoDatabase = this.mongoClient.getDatabase("nukkit");
            this.playersData = this.mongoDatabase.getCollection("players");

            instance.getLogger().info("MongoDB Connected");
        });
    }

    public void disconnect(SafariPlugin instance) {
        this.mongoClient.close();
        instance.getLogger().info("MongoDB Disconnected");
    }

    public void initializePlayer(SafariPlugin instance, Player player) {
        CompletableFuture.runAsync(() -> {
            Document existing = this.playersData.find(new Document("uuid", player.getUniqueId())).first();
            if (existing == null) {
                Document document = new Document("uuid", player.getUniqueId())
                        .append("name", player.getName())
                        .append("money", 100);
                this.playersData.insertOne(document);
                SafariPlayer safariPlayer = SafariPlayer.fromDocument(document);
                instance.addPlayer(player.getUniqueId(), safariPlayer);
                instance.getLogger().info("PlayerInfo created :)");
            } else {
                SafariPlayer safariPlayer = SafariPlayer.fromDocument(existing);
                instance.addPlayer(player.getUniqueId(), safariPlayer);
                instance.getLogger().info("PlayerInfo fetched :)");
            }
        });
    }
}