package tfar.teamlife.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;
import tfar.teamlife.TeamLife;
import tfar.teamlife.network.client.S2CClearModTeamPacket;
import tfar.teamlife.network.client.S2CModTeamPacket;
import tfar.teamlife.platform.Services;

import java.util.*;

public class ModTeamsServer extends SavedData {

    private final List<ModTeam> modTeamList = new ArrayList<>();
    private final ServerLevel serverLevel;

    public ModTeamsServer(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
    }

    @Nullable
    public ModTeam findTeam(ServerPlayer player) {
        return findTeam(player.getGameProfile());
    }

    @Nullable
    public ModTeam findTeam(GameProfile player) {
        for (ModTeam modTeam : modTeamList) {
            if (modTeam.isMember(player)) return modTeam;
        }
        return null;
    }

    public boolean createTeam(String name) {
        Optional<ModTeam> optional = findTeam(name);
        if (optional.isPresent()) return false;
        ModTeam modTeam = ModTeam.create(name);
        modTeamList.add(modTeam);
        setDirty();
        return true;
    }

    public void removeTeamByName(String team) {
        Optional<ModTeam> optional = findTeam(team);
        optional.ifPresent(modTeam -> {
            modTeamList.remove(modTeam);
            for (UUID member : modTeam.getMembers()) {
                ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(member);
                if (player != null) {
                    updateClient(player,null);
                }
            }
        });
        setDirty();
    }

    public Optional<ModTeam> findTeam(String name) {
        return modTeamList.stream().filter(modTeam -> modTeam.name.equals(name)).findFirst();
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

    public void updateClient(ServerPlayer player) {
        ModTeam modTeam = findTeam(player);
        updateClient(player,modTeam);
    }

    public void updateClient(ServerPlayer player,@Nullable ModTeam team) {
        if (team != null) {
            Services.PLATFORM.sendToClient(new S2CModTeamPacket(team),player);
        } else {
            Services.PLATFORM.sendToClient(S2CClearModTeamPacket.INSTANCE,player);
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
        compoundTag.put(TeamLife.MOD_ID, listTag);
        return compoundTag;
    }


    public void addMembers(String team, Collection<ServerPlayer> playerList) {
        findTeam(team).ifPresent(modTeam -> playerList.forEach(player -> {
            modTeam.getMembers().add(player.getUUID());
            updateClient(player,modTeam);
        }));
        setDirty();
    }

    public void removeMembers(String team,Collection<ServerPlayer> playerList) {
        findTeam(team).ifPresent(modTeam -> playerList.forEach(player -> {
            modTeam.getMembers().remove(player.getUUID());
            updateClient(player,null);
        }));
    }

}
