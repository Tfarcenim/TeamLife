package tfar.teamlife.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import tfar.teamlife.world.inventory.TeamInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModTeam {

    private final List<UUID> members = new ArrayList<>();

    public static float STARTING_HEALTH = 20;
    private final HolderLookup.Provider provider;
    public float health = STARTING_HEALTH;
    public float maxHealth = STARTING_HEALTH;
    public String name;

    public TeamInventory teamInventory = new TeamInventory();


    public ModTeam(HolderLookup.Provider provider) {
        this.provider = provider;
    }

    public static ModTeam create(String name,HolderLookup.Provider provider) {
        ModTeam modTeam = new ModTeam(provider);
        modTeam.name = name;
        return modTeam;
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
        compoundTag.put("teamInventory", teamInventory.createTag(provider));
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


    public void toPacket(FriendlyByteBuf buf) {
        int size = members.size();
        buf.writeInt(size);
        for (UUID gameProfile : members) {
            buf.writeUUID(gameProfile);
        }
        buf.writeFloat(health);
        buf.writeFloat(maxHealth);
        buf.writeUtf(name);
    }

    public static ModTeam fromPacket(FriendlyByteBuf buf) {
        ModTeam modTeam = new ModTeam(null);
        int size = buf.readInt();
        for (int i = 0; i<size;i++) {
            UUID gameProfile = buf.readUUID();
            modTeam.members.add(gameProfile);
        }
        modTeam.health = buf.readFloat();
        modTeam.maxHealth = buf.readFloat();
        modTeam.name = buf.readUtf();
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
        modTeam.teamInventory.fromTag(tag.getList("teamInventory",Tag.TAG_COMPOUND),provider);
        return modTeam;
    }
}
