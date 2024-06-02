package tfar.teamlife.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModTeam {

    private final List<UUID> members = new ArrayList<>();

    public static double STARTING_HEALTH = 20;
    private double health = STARTING_HEALTH;
    private double maxHealth = STARTING_HEALTH;

    public static ModTeam create() {
        ModTeam modTeam = new ModTeam();
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
        buf.writeDouble(health);
        buf.writeDouble(maxHealth);
    }

    public static ModTeam fromPacket(FriendlyByteBuf buf) {
        ModTeam modTeam = new ModTeam();
        int size = buf.readInt();
        for (int i = 0; i<size;i++) {
            UUID gameProfile = buf.readUUID();
            modTeam.members.add(gameProfile);
        }
        modTeam.health = buf.readDouble();
        modTeam.maxHealth = buf.readDouble();
        return modTeam;
    }

    public static ModTeam loadStatic(CompoundTag tag) {
        ModTeam modTeam = new ModTeam();
        ListTag listTag = tag.getList("members", Tag.TAG_COMPOUND);
        for (Tag tag1 : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag1;
            modTeam.members.add(compoundTag.getUUID("member"));
        }

        modTeam.health = tag.getInt("health");
        modTeam.maxHealth = tag.getInt("maxHealth");

        return modTeam;
    }
}
