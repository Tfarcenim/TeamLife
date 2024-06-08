package tfar.teamlife.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import tfar.teamlife.item.Artifact;
import tfar.teamlife.world.inventory.TeamInventory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModTeam {

    private final List<UUID> members = new ArrayList<>();

    public static float STARTING_HEALTH = 20;
    private final HolderLookup.Provider provider;
    public float health = STARTING_HEALTH;
    public float maxHealth = STARTING_HEALTH;
    public String name;
    public UUID uuid;

    public TeamInventory teamInventory = new TeamInventory();
    public Set<Item> usableArtifacts = new HashSet<>();


    public ModTeam(HolderLookup.Provider provider) {
        this.provider = provider;
    }

    public static ModTeam create(String name,HolderLookup.Provider provider) {
        ModTeam modTeam = new ModTeam(provider);
        modTeam.name = name;
        modTeam.uuid = Mth.createInsecureUUID();
        return modTeam;
    }

    public void defaultHealth() {
        maxHealth = members.size() * STARTING_HEALTH;
        health = Math.min(health,maxHealth);
    }

    public void addArtifact(Artifact artifact) {
        usableArtifacts.addAll(artifact.usable());
    }

    public void removeArtifact(Artifact artifact) {
        usableArtifacts.removeAll(artifact.usable());
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ModTeam> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public ModTeam decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
            return fromPacket(registryFriendlyByteBuf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf registryFriendlyByteBuf, ModTeam modTeam) {
            modTeam.toPacket(registryFriendlyByteBuf);
        }
    };



    public CompoundTag save() {
        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (UUID entry : members) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("member", entry);
            listTag.add(tag);
        }
        compoundTag.put("members", listTag);

        compoundTag.putDouble("health",health);
        compoundTag.putDouble("maxHealth",maxHealth);
        compoundTag.putString("name",name);
        compoundTag.putUUID("uuid",uuid);
        compoundTag.put("teamInventory", teamInventory.createTag(provider));

        ListTag listTag1 = new ListTag();

        for (Item item : usableArtifacts) {
            listTag1.add(StringTag.valueOf(BuiltInRegistries.ITEM.getKey(item).toString()));
        }

        compoundTag.put("usableArtifacts",listTag1);

        return compoundTag;
    }

    public List<UUID> getMembers() {
        return members;
    }


    public boolean isFull() {
        return getMembers().size() >= 4;
    }

    public boolean isMember(Player player) {
        return isMember(player.getGameProfile());
    }


    public boolean isMember(GameProfile player) {
        return members.contains(player.getId());
    }


    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> ITEM_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ITEM);


    public void toPacket(RegistryFriendlyByteBuf buf) {
        encodeList(buf,members, buf::writeUUID);
        buf.writeFloat(health);
        buf.writeFloat(maxHealth);
        buf.writeUtf(name);
        buf.writeUUID(uuid);
        encodeList(buf,usableArtifacts,item -> ITEM_STREAM_CODEC.encode(buf,item.builtInRegistryHolder()));
    }

    <T> void encodeList(RegistryFriendlyByteBuf buf,Collection<T> collection, Consumer<T> consumer) {
        buf.writeInt(collection.size());
        collection.forEach(consumer::accept);
    }

   static  <T> void decodeList(RegistryFriendlyByteBuf buf, Collection<T> collection, Supplier<T> consumer) {
        int size = buf.readInt();
        for (int i = 0; i < size;i++) {
            collection.add(consumer.get());
        }
    }

    public static ModTeam fromPacket(RegistryFriendlyByteBuf buf) {
        ModTeam modTeam = new ModTeam(null);

        decodeList(buf,modTeam.members, buf::readUUID);

        modTeam.health = buf.readFloat();
        modTeam.maxHealth = buf.readFloat();
        modTeam.name = buf.readUtf();
        modTeam.uuid = buf.readUUID();

        decodeList(buf,modTeam.usableArtifacts, () -> ITEM_STREAM_CODEC.decode(buf).value());
        return modTeam;
    }

    public static ModTeam loadStatic(CompoundTag tag,HolderLookup.Provider provider) {
        ModTeam modTeam = new ModTeam(provider);
        ListTag listTag = tag.getList("members", Tag.TAG_COMPOUND);
        for (Tag tag1 : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag1;
            modTeam.members.add(compoundTag.getUUID("member"));
        }
        modTeam.health = tag.getInt("health");
        modTeam.maxHealth = tag.getInt("maxHealth");
        modTeam.name = tag.getString("name");
        modTeam.uuid = tag.getUUID("uuid");
        modTeam.teamInventory.fromTag(tag.getList("teamInventory",Tag.TAG_COMPOUND),provider);

        ListTag listTag1 = tag.getList("usableArtifacts",Tag.TAG_STRING);

        for (Tag tag1 : listTag1) {
            StringTag stringTag = (StringTag) tag1;
            modTeam.usableArtifacts.add(BuiltInRegistries.ITEM.get(new ResourceLocation(stringTag.getAsString())));
        }

        return modTeam;
    }
}
