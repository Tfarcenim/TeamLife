package tfar.teamlife.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;
import tfar.teamlife.TeamLife;

import java.util.ArrayList;
import java.util.List;

public class ModTeamsServer extends SavedData {

    private final List<ModTeam> modTeamList = new ArrayList<>();

    public ModTeamsServer(ServerLevel p295840) {

    }

    public void kickPlayer(MinecraftServer server, ModTeam modTeam, GameProfile member, boolean voluntary) {

        ServerPlayer player = server.getPlayerList().getPlayer(member.getId());

        boolean kick = modTeam.getMembers().remove(member);

        if (kick) {
            if (player != null) {
                if (voluntary) {
                } else {
                }
                syncToClientPlayer(player);
            }
            //PartyCommands.forAllInParty(server, modTeam, this::syncToClientPlayer);
            setDirty();
        }
    }

    public void disbandParty(MinecraftServer server, ModTeam modTeam) {
        modTeamList.remove(modTeam);
        //PartyCommands.forAllInParty(server, modTeam, this::syncToClientPlayer);
        setDirty();
    }

    public void syncToClientPlayer(ServerPlayer player) {
        ModTeam modTeam = findParty(player);
        //Services.PLATFORM.sendToClient(modTeam != null ? new S2CPartyPacket(modTeam): new S2CClearPartyPacket(),player);
    }

    @Nullable
    public ModTeam findParty(ServerPlayer player) {
        return findParty(player.getGameProfile());
    }

    @Nullable
    public ModTeam findParty(GameProfile player) {
        for (ModTeam modTeam : modTeamList) {
            if (modTeam.isMember(player)) return modTeam;
        }
        return null;
    }

    public ModTeam createTeam() {
        ModTeam modTeam = ModTeam.create();
        modTeamList.add(modTeam);
        setDirty();
        return modTeam;
    }

    public void load(CompoundTag tag) {
        modTeamList.clear();
        ListTag listTag = tag.getList(TeamLife.MOD_ID, Tag.TAG_COMPOUND);
        for (Tag tag1 : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag1;
            ModTeam modTeam = ModTeam.loadStatic(compoundTag);
            modTeamList.add(modTeam);
        }
    }

    @Nullable
    public static ModTeamsServer getInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(factory(serverLevel),TeamLife.MOD_ID);
    }

    @Nullable
    public static ModTeamsServer getDefaultInstance(MinecraftServer server) {
        return getInstance(server.overworld());
    }


    public static ModTeamsServer getOrCreateInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(factory(serverLevel), TeamLife.MOD_ID);
    }

    public static ModTeamsServer getOrCreateDefaultInstance(MinecraftServer server) {
        return getOrCreateInstance(server.overworld());
    }

    public static SavedData.Factory<ModTeamsServer> factory(ServerLevel level) {
        return new SavedData.Factory<>(() -> new ModTeamsServer(level), (tag, provider) -> loadStatic(level, tag), null);
    }

    public static ModTeamsServer loadStatic(ServerLevel level,CompoundTag compoundTag) {
        ModTeamsServer id = new ModTeamsServer(level);
        id.load(compoundTag);
        return id;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        for (ModTeam modTeam : modTeamList) {
            listTag.add(modTeam.save());
        }
        //pCompoundTag.put(Parties.MOD_ID, listTag);
        return compoundTag;
    }
}
