package cloud.souris.aytos.mc.safari.npcs;

import cloud.souris.aytos.mc.safari.SafariPlugin;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.*;

import java.nio.charset.StandardCharsets;

public class EntityUtils {
    public static CompoundTag nbt(Player p, boolean isHuman, String name) {
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<>("Pos")
                        .add(new DoubleTag("", p.x))
                        .add(new DoubleTag("", p.y))
                        .add(new DoubleTag("", p.z)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", (float) p.getYaw()))
                        .add(new FloatTag("", (float) p.getPitch())))
                .putBoolean("Invulnerable", true)
                .putString("NameTag", name)
                .putList(new ListTag<StringTag>("Commands"))// tady commandy co se maji spustit kdyz dostane damage od hrace
                .putList(new ListTag<StringTag>("PlayerCommands"))// tady commandy co se maji spustit za hrace kdyz od nej dostane damage
                .putBoolean("npc", true)
                .putFloat("scale", 1);
        if (isHuman) {
            CompoundTag skinTag = new CompoundTag()
                    .putByteArray("Data", p.getSkin().getSkinData().data)
                    .putInt("SkinImageWidth", p.getSkin().getSkinData().width)
                    .putInt("SkinImageHeight", p.getSkin().getSkinData().height)
                    .putString("ModelId", p.getSkin().getSkinId())
                    .putString("CapeId", p.getSkin().getCapeId())
                    .putByteArray("CapeData", p.getSkin().getCapeData().data)
                    .putInt("CapeImageWidth", p.getSkin().getCapeData().width)
                    .putInt("CapeImageHeight", p.getSkin().getCapeData().height)
                    .putByteArray("SkinResourcePatch", p.getSkin().getSkinResourcePatch().getBytes(StandardCharsets.UTF_8))
                    .putByteArray("GeometryData", p.getSkin().getGeometryData().getBytes(StandardCharsets.UTF_8))
                    .putByteArray("AnimationData", p.getSkin().getAnimationData().getBytes(StandardCharsets.UTF_8))
                    .putBoolean("PremiumSkin", p.getSkin().isPremium())
                    .putBoolean("PersonaSkin", p.getSkin().isPersona())
                    .putBoolean("CapeOnClassicSkin", p.getSkin().isCapeOnClassic());
            nbt.putCompound("Skin", skinTag);
            nbt.putBoolean("ishuman", true);
            nbt.putString("Item", p.getInventory().getItemInHand().getName());
            nbt.putString("Helmet", p.getInventory().getHelmet().getName());
            nbt.putString("Chestplate", p.getInventory().getChestplate().getName());
            nbt.putString("Leggings", p.getInventory().getLeggings().getName());
            nbt.putString("Boots", p.getInventory().getBoots().getName());
        }
        return nbt;
    }

    public static void spawnNPC(SafariPlugin instance, Player author, String npcType, String npcName) {
        CompoundTag nbt = nbt(author, (npcType.equalsIgnoreCase(HumanNPC.class.getSimpleName())), npcName);
        Entity entity = Entity.createEntity(npcType, author.chunk, nbt);
        entity.setNameTag("\u00a74[\u00a79NPC\u00a74]\u00a7f " + npcName);
        entity.setNameTagVisible(true);
        entity.setNameTagAlwaysVisible(true);
        entity.spawnToAll();

        instance.addNPC(npcName, entity);
        author.sendMessage("NPC vytvořeno s ID " + entity.getId() + " a jménem " + entity.getName());
    }

    public static void teleportPlayerToWorldSpawn(SafariPlugin instance, Player player) {
        Position worldSpawnPosition = player.getLevel().getSafeSpawn();
        instance.getLogger().info("WorldSpawn is: " + worldSpawnPosition.toString());
        instance.getLogger().info("Player is: " + player.getPosition().toString());
        instance.getLogger().info("Should teleport? :)");
    }

    public static void teleportPlayerToHome(SafariPlugin instance, Player player) {
        Position homeSpawnPosition = player.getSpawn();
        instance.getLogger().info("HomeSpawn is: " + homeSpawnPosition.toString());
        instance.getLogger().info("Player is: " + player.getPosition().toString());
        instance.getLogger().info("Should teleport? :)");
    }

    public static void setPlayersHome(SafariPlugin instance, Player player) {
        Position oldHome = player.getSpawn();
        instance.getLogger().info("Old home spawn is: " + oldHome.toString());
        instance.getLogger().info("Going to set it to: " + player.getPosition().toString());
        instance.getLogger().info("Should teleport? :)");
    }
}
