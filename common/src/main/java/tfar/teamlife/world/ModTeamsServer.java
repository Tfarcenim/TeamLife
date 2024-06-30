package tfar.teamlife.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
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

    AttributeModifier modifyHealth;

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

    public void setDefaultHealthModifierAndSync(double mod) {

        if (modifyHealth!= null) {
            serverLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(modifyHealth.id());
            });
        };

        if (mod != 0) {
            modifyHealth = new AttributeModifier(uuid,"default_health_modifier",mod, AttributeModifier.Operation.ADD_VALUE);
            serverLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
                player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(modifyHealth);
            });
        }

        else {
            modifyHealth = null;
        }

        setDirty();
    }

    @Nullable
    public ModTeam findTeamByUUID(UUID teamUUID) {
        for (ModTeam modTeam : modTeamList) {
            if (teamUUID.equals(modTeam.uuid)) {
                return modTeam;
            }
        }
        return null;
    }

    public boolean createTeam(String name) {
        ModTeam modTeam = findTeam(name);
        if (modTeam != null) return false;
        modTeam = ModTeam.create(name, serverLevel.registryAccess());
        modTeamList.add(modTeam);
        setDirty();
        return true;
    }

    public void removeTeamByName(String team) {
        ModTeam modTeam = findTeam(team);
        if (modTeam != null) {
            modTeamList.remove(modTeam);
            for (UUID member : modTeam.getMembers()) {
                ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(member);
                if (player != null) {
                    updateClient(player, null);
                }
            }
            setDirty();
        }
    }

    public List<ModTeam> getModTeamList() {
        return modTeamList;
    }

    public ModTeam findTeam(String name) {
        return modTeamList.stream().filter(modTeam -> modTeam.name.equals(name)).findFirst().orElse(null);
    }

    public static final UUID uuid = UUID.fromString("6b996527-1731-4340-94f0-c7076ad4159f");

    public void load(CompoundTag tag) {
        modTeamList.clear();
        ListTag listTag = tag.getList(TeamLife.MOD_ID, Tag.TAG_COMPOUND);
        for (Tag tag1 : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag1;
            ModTeam modTeam = ModTeam.loadStatic(compoundTag, serverLevel.registryAccess());
            modTeamList.add(modTeam);
        }
        if (tag.contains("default_health_modifier")) {
            modifyHealth = new AttributeModifier(uuid,"default_health_modifier",tag.getDouble("default_health_modifier"), AttributeModifier.Operation.ADD_VALUE);
        }
    }

    public void updateClient(ServerPlayer player) {
        ModTeam modTeam = findTeam(player);
        updateClient(player, modTeam);
    }

    public void modifyDefaultHealth(Player player) {
        AttributeModifier modifier = player.getAttribute(Attributes.MAX_HEALTH).getModifier(uuid);
        if (modifyHealth != null) {
            if (modifier == null || modifier.amount() != modifyHealth.amount()) {
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(uuid);
            player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(modifyHealth);
            }
        } else {
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(uuid);
        }
    }

    public void updateClient(ServerPlayer player, @Nullable ModTeam team) {
        if (team != null) {
            Services.PLATFORM.sendToClient(new S2CModTeamPacket(team), player);
        } else {
            Services.PLATFORM.sendToClient(S2CClearModTeamPacket.INSTANCE, player);
        }
    }

    public void updateTeam(ModTeam modTeam) {
        for (UUID member : modTeam.getMembers()) {
            ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(member);
            if (player != null) {
                updateClient(player, modTeam);
            }
        }
    }

    public void adjustHealth(ModTeam modTeam, float amount) {
        modTeam.health = Mth.clamp(modTeam.health + amount, 0, modTeam.maxHealth);
        updateTeam(modTeam);
        setDirty();
    }

    public void adjustMaxHealth(ModTeam modTeam, float amount) {
        modTeam.maxHealth += amount;
        updateTeam(modTeam);
        setDirty();
    }

    public void setMaxHealth(ModTeam modTeam, float amount) {
        modTeam.maxHealth = amount;
        updateTeam(modTeam);
        setDirty();
    }

    public void refillMaxHealth(ModTeam modTeam) {
        adjustHealth(modTeam, modTeam.maxHealth);
    }

    @Nullable
    public static ModTeamsServer getInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(factory(serverLevel), TeamLife.MOD_ID);
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

    public static ModTeamsServer loadStatic(ServerLevel level, CompoundTag compoundTag) {
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

        if (modifyHealth != null) {
            compoundTag.putDouble("default_health_modifier",modifyHealth.amount());
        }

        return compoundTag;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public void addMembers(String team, Collection<ServerPlayer> playerList) {
        ModTeam modTeam = findTeam(team);
        if (modTeam != null) {
            playerList.forEach(player -> {
                modTeam.getMembers().add(player.getUUID());
                updateClient(player, modTeam);
            });

            modTeam.defaultHealth();
            updateTeam(modTeam);

            setDirty();
        }
    }

    public void removeMembers(String team, Collection<ServerPlayer> playerList) {
        ModTeam modTeam = findTeam(team);
        if (modTeam != null) {
            playerList.forEach(player -> {
                modTeam.getMembers().remove(player.getUUID());
                updateClient(player, null);
            });

            modTeam.defaultHealth();
            updateTeam(modTeam);

            setDirty();
        }
    }
}
